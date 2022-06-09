package com.cervest

import java.nio.file.{Files, Path}
import org.apache.spark.sql._
import org.apache.spark.{ SparkContext }
import org.scalactic.Tolerance
import org.scalatest._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

trait TestEnvironment extends AnyFunSpec
  with Matchers with Inspectors with Tolerance {

  lazy val scratchDir: Path = {
    val outputDir = Files.createTempDirectory("cervest-")
    outputDir.toFile.deleteOnExit()
    outputDir
  }

  def sparkMaster = "local[*, 2]"

  implicit lazy val spark: SparkSession = {
    val spark = SparkSession.builder().master("local[*]").
      appName("Test Context").
      config("spark.sql.shuffle.partitions", "200").
      config("spark.hadoop.parquet.block.size", 64*1024*1024 /* bytes */).
      config("spark.hadoop.parquet.page.size", 1*1024*1024 /* bytes */).
      config("spark.hadoop.parquet.enable.dictionary", true).
      config("spark.hadoop.fs.s3a.aws.credentials.provider", "com.amazonaws.auth.DefaultAWSCredentialsProviderChain").
      getOrCreate()
      spark.sparkContext.setLogLevel("ERROR")
      spark
    }
  implicit def sparkContext: SparkContext = spark.sparkContext
}