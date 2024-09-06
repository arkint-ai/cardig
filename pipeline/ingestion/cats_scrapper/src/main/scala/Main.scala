import cats.effect.{IO, IOApp}
import fs2.Stream

object Main extends IOApp.Simple {

  val bmwUrl: String =
    "https://www.standvirtual.com/carros-novos/pesquisar/bmw/?OT=1"

  val scraper: Stream[IO, Unit] = {
    Scraper
      .fetchProducts(bmwUrl, 1)
      .evalMap(product => IO.println(s"=> $product"))
      .onFinalize(IO.println("[LOGGER] Scraping done"))
  }

  override def run: IO[Unit] = scraper.compile.drain
}
