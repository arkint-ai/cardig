import cats.effect.{IO, IOApp}
import fs2.Stream

object Main extends IOApp.Simple {

  val bmwUrl: String =
    "https://www.standvirtual.com/carros-novos/pesquisar/bmw/?OT=1"

  def scraper: IO[Unit] = {
    for {
      products <- Scraper.scrapeProducts(bmwUrl, 1)
      _ <- IO { products.foreach(println) }
    } yield ()
  }

  override def run: IO[Unit] = scraper
}
