package com.cervest

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{collect_list, explode, lag}

class S3ParquetSpec extends TestEnvironment {
  it("reads"){
    val url = "s3a://cervest-earthcore-platform-scratch-data/flooding/riverine-flooding/v0/lvl4_idx=595687070324752383/"
    val df = spark.read.parquet(url)
    df.show()
  }
}
