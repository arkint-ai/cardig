import org.jsoup._
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.HttpStatusException

import scala.jdk.CollectionConverters._

import cats.effect.{IO, Resource}

object Scraper {
  def fetchProducts(doc: Document): List[Element] = {
    doc.select(".car-card-info").asScala.toList
  }

  def selectProductTitles(products: List[Element]): List[String] = {
    products
      .map { product =>
        val productTitle = product.select("h3").text()
        if (productTitle.nonEmpty) Some(productTitle) else None
      }
      .collect { case Some(productTitle) =>
        productTitle
      }

  }

  def scrapeProducts(
      baseUrl: String,
      pageNumber: Int
  ): IO[List[String]] = {

    val pageValue: String = s"&pag=$pageNumber"
    val url: String       = baseUrl + pageValue
    val nextPageNumber    = pageNumber + 1

    connection(url)
      .use { doc =>
        val products      = fetchProducts(doc)
        val productTitles = selectProductTitles(products)
        for {
          productsAcc <-
            if (productTitles.nonEmpty) {
              IO {
                println(
                  s"[LOGGER] Found ${productTitles.size} products on $url"
                )
              } >>
              scrapeProducts(baseUrl, pageNumber + 1)
                .map(nextPageProducts => productTitles ++ nextPageProducts)
            } else {
              IO.pure { List.empty[String] }
            }
        } yield productsAcc
      }
      .handleErrorWith {
        case e: HttpStatusException =>
          IO {
            println(s"[LOGGER] Error ${e.getStatusCode}: ${e.getMessage}")
          } >> IO.pure(List.empty[String])
        case e =>
          IO { println(s"[LOGGER] Error: ${e.getMessage}") } >> IO.pure(
            List.empty[String]
          )
      }
  }

  def connection(url: String): Resource[IO, Document] =
    Resource.make(
      IO { println(s"[LOGGER] Estabilishing connection on $url") }
        .flatMap(_ => IO { Jsoup.connect(url).get() })
    )(_ => IO { println(s"[LOGGER] Closed connection on $url") })

}
