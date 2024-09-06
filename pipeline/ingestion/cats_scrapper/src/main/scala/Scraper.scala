import org.jsoup._
import org.jsoup.HttpStatusException

import scala.jdk.CollectionConverters._

import cats.effect.{IO, Resource}

import fs2.Stream

// TODO: replace jsoup with this
// https://github.com/ruippeixotog/scala-scraper
// TODO: cats logger to stdout instead of println
object Scraper {

  def fetchProducts(
      baseUrl: String,
      pageNumber: Int
  ): Stream[IO, String] = {

    val pageValue: String = s"&pag=$pageNumber"
    val url: String       = baseUrl + pageValue

    Stream
      .resource(createConnection(url))
      .flatMap { doc =>
        val products      = doc.select(".car-card-info").asScala.toList
        val productTitles = products.map(_.select("h3").text())

        val nextPageLink   = doc.select("div.pagination-next a[title='NEXT']")
        val nextPageNumber = pageNumber + 1

        val noMoreProducts =
          products.isEmpty || productTitles.isEmpty || nextPageLink.isEmpty
        if (noMoreProducts) {
          Stream.eval(
            IO.println(s"[LOGGER] No more pages after $pageNumber for $baseUrl")
          ) >>
            Stream.emits(productTitles)
        } else {
          Stream.emits(productTitles) ++ fetchProducts(baseUrl, nextPageNumber)
        }
      }
      .handleErrorWith {
        case e: HttpStatusException if e.getStatusCode == 403 =>
          Stream
            .eval(
              IO.println(s"[LOGGER] Error 403 - Forbidden at page $pageNumber")
            )
            .drain
        case e: HttpStatusException if e.getStatusCode == 400 =>
          Stream
            .eval(
              IO.println(
                s"[LOGGER] Error 400 - Bad Request at page $pageNumber"
              )
            )
            .drain
        case e =>
          Stream.eval(IO.println(s"[LOGGER] Error Unknown - $e")).drain
      }
  }

  private def createConnection(
      url: String
  ): Resource[IO, org.jsoup.nodes.Document] =
    Resource.make(
      IO(Jsoup.connect(url).get())
    )(_ => IO.println(s"[LOGGER] Closed connection for $url"))

}
