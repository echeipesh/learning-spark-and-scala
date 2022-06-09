package com.cervest

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.util.Properties

/**
 * Utility trait for Commands that provides SparkSession instance
 */
trait SparkCommand {

  implicit lazy val spark: SparkSession = {
    val conf: SparkConf = new SparkConf()
      .setIfMissing("spark.master", "local[*]")
      .setAppName("learning")
      .set("spark.sql.shuffle.partitions", "101")
      .set("spark.hadoop.parquet.block.size", (64*1024*1024).toString)
      .set("spark.hadoop.parquet.page.size", (1*1024*1024).toString)
      .set("spark.hadoop.parquet.enable.summary-metadata", "false")
      .set("spark.executionEnv.AWS_PROFILE", Properties.envOrElse("AWS_PROFILE", "default"))

    SparkSession.builder().config(conf).getOrCreate()
  }

  def withSpark[A](job: SparkSession => A): A = {
    try {
      job(spark)
    } finally {
      spark.stop()
    }
  }
}
