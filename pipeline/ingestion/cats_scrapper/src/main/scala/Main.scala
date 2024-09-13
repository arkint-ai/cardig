import cats.effect.{IO, IOApp}
import cats.implicits.toTraverseOps

object Main extends IOApp.Simple {

  def scraper: IO[Unit] = {
    val bmwURL: String =
      "https://www.standvirtual.com/carros-novos/pesquisar/bmw/?OT=1"

    for {
      productTitles <- Scraper.scrapeProducts(bmwURL)
      _             <- productTitles.traverse(title => IO { println(title) })
    } yield ()
  }

  def run: IO[Unit] = scraper
}
