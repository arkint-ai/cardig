import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val bmwUrl = "https://www.standvirtual.com/carros-novos/pesquisar/bmw/?OT=1"
  override def run: IO[Unit] = Scrapper.fetchProducts(bmwUrl, 1)
}
