// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
import org.jsoup.nodes.Document

import munit.CatsEffectSuite

import cats.effect.{IO, SyncIO}

class FileIOTest extends munit.CatsEffectSuite {
  def writeAndRead(filePath: String, content: List[String]): IO[List[String]] = {
    for {
      _             <- FileIO.write(filePath, content)
      outputData    <- FileIO.read(filePath)
    } yield outputData
  }

  test("read and write file operations") {
    val mockData: List[String] = List("line1", "line2", "line3")
    val filePath: String = "/tmp/FileIOTest_mock.txt"

    writeAndRead(filePath, mockData).flatMap { outputData=>
      IO {
        assertEquals(outputData, mockData)
      }
    }
  }

}
