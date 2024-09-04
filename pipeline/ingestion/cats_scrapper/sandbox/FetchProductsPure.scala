import org.jsoup._
import org.jsoup.HttpStatusException
import scala.jdk.CollectionConverters._
import cats.effect.IO
import cats.Traverse
import cats.implicits._

def fetchProducts(baseUrl: String, pageNumber: Int): IO[List[String]] = {
  if (pageNumber == 420) {
    IO.pure(List.empty[String])
  } else {
    val pageValue = s"&pag=$pageNumber"
    val url       = baseUrl + pageValue
    IO(Jsoup.connect(url).get()).attempt.flatMap {
      case Right(doc) =>
        val products = doc.select(".car-card-info").asScala.toList
        if (products.isEmpty) {
          IO.pure(List.empty[String])
        } else {
          val titles = products.map { product =>
            val productTitle = product.select("h3").text()
            productTitle
          }

          val nextPageLink = doc.select("div.pagination-next a[title='NEXT']")
          if (nextPageLink.isEmpty) {
            IO.pure(titles)
          } else {
            fetchProducts(baseUrl, pageNumber + 1).map { nextTitles =>
              titles ++ nextTitles
            }
          }
        }
      case Left(e: HttpStatusException) if e.getStatusCode == 403 =>
        IO.pure(List.empty[String])
      case Left(e: HttpStatusException) if e.getStatusCode == 400 =>
        IO.pure(List.empty[String])
      case Left(e) =>
        IO.pure(List.empty[String])
    }
  }
}
