import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {

  def scraper: IO[Unit] = {
    val bmwURL: String =
      "https://www.standvirtual.com/carros-novos/pesquisar/bmw/?OT=1"

    Scraper
      .scrapeProducts(bmwURL)
      .flatMap(productTitle => IO.println(productTitle))

  }

  override def run: IO[Unit] = scraper
}
