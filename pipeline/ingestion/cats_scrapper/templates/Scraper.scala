import org.jsoup._
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.HttpStatusException

import scala.jdk.CollectionConverters._

import cats.effect.{IO, Resource}
import cats.syntax.all._

object Scraper {
  def selectProducts(doc: Document): List[Element] = {
    doc.select(".car-card-info").asScala.toList
  }

  def selectProductTitles(products: List[Element]): List[String] = {
    products.map { product =>
      val title = product.select("h3").text()
      if (title.nonEmpty) Some(title) else None
    }
  }

  def scrapeProducts(
      baseUrl: String,
      pageNumber: Int
  ): IO[List[String]] = {

    val pageValue: String = s"&pag=$pageNumber"
    val url: String       = baseUrl + pageValue
    val nextPageNumber    = pageNumber + 1

    fetch
      .eval(url) { doc =>
        for {
          products      = fetchProducts(doc)
          productTitles = selectProductTitles(products)
          _ <- IO {
            println(
              s"[LOGGER] Found ${productTitles.size} products on page $pageNumber"
            )
          }

          nextPageProducts <-
            if (productTitles.nonEmpty) {
              scrapeProducts(baseUrl, pageNumber + 1)
            } else {
              IO.pure(List.empty[String])
            }
        } yield productTitles ++ nextPageProducts

      }
      .handleErrorWith {
        case e: HttpStatusException =>
          IO {
            println(s"[LOGGER] Error ${e.getStatusCode}: ${e.getMessage}")
          }
            .as(List.empty[String])
        case e =>
          IO { println(s"[LOGGER] Error: ${e.getMessage}") }
            .as(List.empty[String])
      }
  }

  def fetch(url: String): Resource[IO, org.jsoup.nodes.Document] =
    Resource
      .make(
        IO { println(s"[LOGGER] Establishing connection on $url") } >>
          IO { Jsoup.connect(url).get() }
      )(doc => IO { println(s"[LOGGER] Closed connection on $url") })
      .use { _ =>
        IO { println(s"[LOGGER] Successfully fetched content from $url") }
      }
}
