name := "learning"
organization := "com.cervest"
version := "0.1.0"

scalaVersion := "2.12.12"

libraryDependencies ++= Seq(
  // Match EMR 660: https://docs.aws.amazon.com/emr/latest/ReleaseGuide/emr-660-release.html
  "org.apache.hadoop" % "hadoop-common" % "3.2.1",
  "org.apache.hadoop" % "hadoop-client" % "3.2.1",
  "org.apache.hadoop" % "hadoop-aws" % "3.2.1",
  "org.apache.spark" %% "spark-core" % "3.2.1" % Compile,
  "org.apache.spark" %% "spark-sql" % "3.2.1" % Compile,
  "org.apache.spark" %% "spark-hive" % "3.2.1" % Compile,
  "org.scalatest" %% "scalatest" % "3.2.10" % Test,
)
resolvers ++= Seq()

scalacOptions ++= Seq()

console / initialCommands :=
"""
import java.net._
""".stripMargin

// Fork JVM for test context to avoid memory leaks in Metaspace
Test / fork := true
Test / outputStrategy := Some(StdoutOutput)

// Settings for sbt-assembly plugin which builds fat jars for spark-submit
assembly / assemblyMergeStrategy := {
 case "reference.conf"   => MergeStrategy.concat
 case "application.conf" => MergeStrategy.concat
 case PathList("META-INF", xs @ _*) =>
   xs match {
     case ("MANIFEST.MF" :: Nil) =>
       MergeStrategy.discard
     case ("services" :: _ :: Nil) =>
       MergeStrategy.concat
     case ("javax.media.jai.registryFile.jai" :: Nil) | ("registryFile.jai" :: Nil) | ("registryFile.jaiext" :: Nil) =>
       MergeStrategy.concat
     case (name :: Nil) if name.endsWith(".RSA") || name.endsWith(".DSA") || name.endsWith(".SF") =>
       MergeStrategy.discard
     case _ =>
       MergeStrategy.first
     }
 case _ => MergeStrategy.first
}


assembly / assemblyShadeRules := {
 val shadePackage = "earth.ceervest.shaded"
 Seq(
   ShadeRule.rename("shapeless.**" -> s"$shadePackage.shapeless.@1").inAll,
   ShadeRule.rename("cats.kernel.**" -> s"$shadePackage.cats.kernel.@1").inAll
 )
}
