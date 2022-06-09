package com.cervest

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.hadoop.conf.Configuration
import org.apache.spark.{SparkConf, SparkException}
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.fs.s3a.S3AFileSystem
import java.net.URI

object Main extends SparkCommand  {

  def main(args: Array[String]): Unit = {
    withSpark { spark =>
      val url = "s3a://cervest-earthcore-platform-scratch-data/flooding/riverine-flooding/v0/lvl4_idx=595687070324752383/"
      val df = spark.read.parquet(url)
      df.show()
    }
  }
}
