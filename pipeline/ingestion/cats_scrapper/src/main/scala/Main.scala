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
  def test: IO[Unit] = {
    Scraper
      .fetchPage("https://archlinux.org/")
      .use { doc =>
        val title     = Scraper.selectDocElements(doc, "h1")
        val titleText = Scraper.selectElementsText(title, "h1")
        IO {
          println(title.head)
          println(titleText.head)
        }
      }
  }

  def run: IO[Unit] = test
}
