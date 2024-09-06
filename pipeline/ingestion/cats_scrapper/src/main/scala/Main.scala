import cats.effect.{IO, IOApp}
import fs2.Stream

// TODO: IO to prouducts to file using fs2
object Main extends IOApp.Simple {

  val bmwUrl: String =
    "https://www.standvirtual.com/carros-novos/pesquisar/bmw/?OT=1"

  val scraper: Stream[IO, Unit] = {
    Scraper
      .fetchProducts(bmwUrl, 1)
      .evalMap(product => IO.println(product))
      .onFinalize(IO.println("=> Main.run finished execution"))
  }

  override def run: IO[Unit] = scraper.compile.drain
}
