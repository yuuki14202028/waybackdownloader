ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "waybackdownloader"
  )

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.client3" %% "core" % "3.10.1", // HTTPクライアントライブラリ
  "ch.qos.logback" % "logback-classic" % "1.5.12"     // ロギングライブラリ（オプション）
)

mainClass := Some("Main") // 実行クラスを指定

