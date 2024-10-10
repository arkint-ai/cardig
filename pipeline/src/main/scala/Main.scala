import org.http4s.ember.client.EmberClientBuilder
import org.http4s.client.Client
import cats.effect.{IO, IOApp, Resource}
import scala.io.Source
import scala.util.{Try, Success, Failure}

object FileOPs {
  def readLines(filePath: String): IO[Either[String, List[String]]] = {
    val fileResource = Resource.fromAutoCloseable(IO(Source.fromFile(filePath)))
    fileResource.use { fileHandle =>
      IO {
        Try(fileHandle.getLines().toList) match {
          case Success(lines)     => Right(lines)
          case Failure(exception) => Left(s"Error: ${exception.getMessage}")
        }
      }
    }
  }

  def printLines(filePath: String): IO[Unit] = {
    readLines(filePath).flatMap {
      case Right(lines) =>
        IO {
          lines.foreach(println)
        }
      case Left(exception) =>
        IO {
          println(exception)
        }
    }
  }
}

object Hello extends IOApp.Simple {

  def printPage(client: Client[IO]): IO[Unit] =
    client
      .expect[String]("https://archlinux.org/")
      .flatMap(IO.println)

  val run: IO[Unit] = EmberClientBuilder
    .default[IO]
    .build
    .use { client =>
      for {
        _ <- printPage(client)
        _ <- FileOPs.printLines("/opt/cardig/data/initial/brands.txt")
      } yield ()
    }
}
