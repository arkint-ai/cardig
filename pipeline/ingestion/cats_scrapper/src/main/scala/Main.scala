import cats.effect.{IO, IOApp}
import cats.syntax.traverse._

object Main extends IOApp.Simple {
  val bmwUrl = "https://www.standvirtual.com/carros-novos/pesquisar/bmw/?OT=1"

  override def run: IO[Unit] = {
    val resultIO: IO[List[(Option[Throwable], Option[String])]] =
      Scrapper.fetchProducts(bmwUrl, 1)

    resultIO.flatMap { results =>
      val (errors, products) = results.partitionMap {
        case (Some(error), None) => Left(error)
        case (None, Some(title)) => Right(title)
        case _ => Left(new Exception("Type error"))
      }

      val handleProducts = products.traverse { title =>
        IO(println(title))
      }

      val handleErrors = if (errors.nonEmpty) {
        IO(println(s"Found ${errors.size} errors:")) *> errors.traverse {
          error =>
            IO(println(s"=> ${error.getMessage}"))
        }
      } else {
        IO.unit
      }

      handleProducts *> handleErrors.void
    }
  }
}
