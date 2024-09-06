import org.jsoup._
import org.jsoup.HttpStatusException
import scala.jdk.CollectionConverters._
import cats.effect.IO
import cats.Traverse
import cats.implicits._

object Scrapper {
  def fetchProducts(
      baseUrl: String,
      pageNumber: Int
  ): IO[List[(Option[Throwable], Option[String])]] = {
    // TODO: this s probly useless at this point, remove 
    if (pageNumber == 42) {
      IO.pure(List.empty)
    } else {
      val pageValue = s"&pag=$pageNumber"
      val url       = baseUrl + pageValue
      IO(Jsoup.connect(url).get()).attempt.flatMap {
        case Right(doc) =>
          val products = doc.select(".car-card-info").asScala.toList
          if (products.isEmpty) {
            IO.pure(List.empty)
          } else {
            Traverse[List]
              .traverse(products) { product =>
                val productTitle = product.select("h3").text()
                IO.pure((None, Some(productTitle)))
              }
              .flatMap { productResults =>
                val nextPageLink =
                  doc.select("div.pagination-next a[title='NEXT']")
                if (nextPageLink.isEmpty) {
                  IO.pure(productResults)
                } else {
                  fetchProducts(baseUrl, pageNumber + 1)
                    .map(nextPageResults => productResults ++ nextPageResults)
                }
              }
          }
        case Left(e: HttpStatusException) if e.getStatusCode == 403 =>
          IO.pure(
            List(
              (
                Some(new Exception(s"[403] Forbidden at page $pageNumber")),
                None
              )
            )
          )
        case Left(e: HttpStatusException) if e.getStatusCode == 400 =>
          IO.pure(
            List(
              (
                Some(new Exception(s"[400] Bad Request at page $pageNumber")),
                None
              )
            )
          )
        case Left(e) =>
          IO.pure(List((Some(e), None)))
      }
    }
  }
}
