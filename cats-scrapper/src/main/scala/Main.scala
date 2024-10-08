import cats.effect.{IO, IOApp}
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.client.Client

object Hello extends IOApp.Simple {

  def printPage(client: Client[IO]): IO[Unit] =
    client
      .expect[String]("https://archlinux.org/")
      .flatMap(IO.println)

  val run: IO[Unit] = EmberClientBuilder
    .default[IO]
    .build
    .use(client => printPage(client))

}
