import sttp.client3.*

import java.io.*
import java.nio.file.{Files, Paths}
import scala.concurrent.duration.DurationInt




object Main extends App {

  // Wayback Machine APIのURL
  private val WAYBACK_API = "https://web.archive.org/cdx/search/cdx"

  // 取得対象のURL
  private val targetUrl = "https://example.com"

  // 保存ディレクトリ
  private val outputDir = "wayback_data"
  Files.createDirectories(Paths.get(outputDir))

  private def getArchivedSnapshots(url: String): List[List[String]] = {
    // APIクライアントを作成
    val request = basicRequest
      .get(uri"$WAYBACK_API?url=$url&output=json&collapse=digest&matchType=prefix")
      .readTimeout(180.seconds)

    val backend = HttpClientSyncBackend()

    // APIリクエストを送信
    val response = request.send(backend)

    response.body match {
      case Right(body) =>
        // JSONをパースしてリストに変換
        val lines = body.split("\n").toList
        lines.tail.map(_.split(",").map { str => str.replace("\"", "") }.toList) // 最初の行はヘッダーなのでスキップ
      case Left(error) =>
        println(s"Error fetching data: $error")
        List.empty[List[String]]
    }
  }

  private def downloadSnapshot(snapshotUrl: String, savePath: String): Unit = {
    // スナップショットをダウンロード
    val backend = HttpClientSyncBackend()
    val request = basicRequest.get(uri"$snapshotUrl").readTimeout(180.seconds)
    val response = request.send(backend)
    response.body match {
      case Right(body) =>
        val file = new File(savePath)
        val writer = new BufferedWriter(new FileWriter(file))
        try {
          writer.write(body)
          println(s"Saved: $savePath")
        } finally {
          writer.close()
        }
      case Left(error) => println(s"Failed to download $snapshotUrl: $error")
    }
  }

  // アーカイブ情報を取得
  private val snapshots = getArchivedSnapshots(targetUrl)

  if (snapshots.isEmpty) {
    println("No snapshots found.")
  } else {
    // 各スナップショットをダウンロード
    snapshots.foreach { snapshot =>
      Thread.sleep(10000)
      println((snapshot, snapshot.size))
      val timestamp = snapshot(2)
      val originalUrl = snapshot(3)
      val snapshotUrl = s"https://web.archive.org/web/$timestamp/$originalUrl"

      // ファイル名を作成
      val fileName = s"$timestamp.html"
      val savePath = Paths.get(outputDir, fileName).toString

      downloadSnapshot(snapshotUrl, savePath)
    }
  }

}