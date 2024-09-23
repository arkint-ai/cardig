import cats.effect.{IO, IOApp}
import cats.implicits._

object Main extends IOApp.Simple {
  def scrapeIt(url: String, filePath: String): IO[Unit] = {
    for {
      productTitles <- StandvirtualScraper.scrapeProducts(url)
      _             <- FileIO.write(filePath, productTitles)
      _             <- IO.println(s"[MAIN] $url data written to $filePath")
    } yield ()
  }

  def run: IO[Unit] = {
    val bmwURL = "https://www.standvirtual.com/carros-novos/pesquisar/bmw/?OT=1"
    val bmwFile = "/tmp/bmw_titles.txt"

    scrapeIt(bmwURL, bmwFile)
  }

}
