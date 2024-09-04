import org.jsoup._
import org.jsoup.HttpStatusException
import scala.jdk.CollectionConverters._
import cats.effect.IO
import cats.Traverse
import cats.implicits._

def fetchProducts(baseUrl: String, pageNumber: Int): IO[Unit] = {
  if (pageNumber == 420) {
    IO(println(s"Reached page number limit of 420."))
  } else {
    val pageValue = s"&pag=$pageNumber"
    val url = baseUrl + pageValue
    IO(Jsoup.connect(url).get()).attempt.flatMap {
      case Right(doc) =>
        val products = doc.select(".car-card-info").asScala.toList
        if (products.isEmpty) {
          IO(println(s"No products found on page $pageNumber."))
        } else {
          Traverse[List].traverse(products) { product =>
            val productTitle = product.select("h3").text()
            IO(println(productTitle))
          }.void.flatMap { _ =>
            val nextPageLink = doc.select("div.pagination-next a[title='NEXT']")
            if (nextPageLink.isEmpty) {
              IO(println(s"No more pages after page $pageNumber."))
            } else {
              fetchProducts(baseUrl, pageNumber + 1)
            }
          }
        }
      case Left(e: HttpStatusException) if e.getStatusCode == 403 =>
       IO(println(s"[403] - Forbidden at page $pageNumber.")) 
      case Left(e: HttpStatusException) if e.getStatusCode == 400 =>
        IO(println(s"[400] - Bad Request at page $pageNumber."))
      case Left(e) =>
        IO(println(s"[ERROR] - ${e.getMessage}"))
      }
    }
}

