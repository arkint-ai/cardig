import cats.effect.{IO, Resource}
import cats.implicits._

import java.nio.file.{Paths, Files, StandardOpenOption}
import java.nio.charset.StandardCharsets

object FileIO {
  def write(filePath: String, content: List[String]): IO[Unit] = {
    Resource
      .fromAutoCloseable(
        IO {
          Files.newBufferedWriter(
            Paths.get(filePath),
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE
          )
        }
      )
      .use { writer =>
        content.traverse_ { line =>
          IO {
            writer.write(line)
            writer.newLine()
          }
        }
      }
  }

  def read(filePath: String): IO[List[String]] = {
    Resource
      .fromAutoCloseable(IO {
        Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)
      })
      .use { reader =>
        IO {
          reader.lines().toArray.toList.asInstanceOf[List[String]]
        }
      }
  }

}
