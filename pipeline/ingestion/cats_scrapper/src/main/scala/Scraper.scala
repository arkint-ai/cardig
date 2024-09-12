import org.jsoup._
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.HttpStatusException

import scala.jdk.CollectionConverters._

import cats.effect.{IO, Resource}

// TODO: make URL a type not String
// https://github.com/lemonlabsuk/scala-uri

object Scraper {
  def selectProducts(doc: Document): List[Element] = {
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

  def buildPageURL(baseURL: String, pageNumber: Int): String = {
    baseURL + s"&pag=${pageNumber}"
  }

  def scrapeProducts(
      baseURL: String,
      pageNumber: Int = 1
  ): IO[List[String]] = {

    val pageURL: String = buildPageURL(baseURL, pageNumber)

    fetchPage(pageURL)
      .use { doc =>
        val productTitles = selectProductTitles(selectProducts(doc))
        for {
          productsAcc <-
            if (productTitles.nonEmpty) {
              IO {
                println(
                  s"[LOGGER] Found ${productTitles.size} products on ${pageURL}"
                )
              } *>
                scrapeProducts(baseURL, pageNumber + 1)
                  .map(nextPageProductTitles => productTitles ++ nextPageProductTitles)
            } else {
              IO.pure { List.empty[String] }
            }
        } yield productsAcc
      }
      .handleErrorWith {
        case e: HttpStatusException =>
          IO {
            println(s"[LOGGER] Error ${e.getStatusCode}: ${e.getMessage}")
          } *> IO.pure(List.empty[String])
        case e =>
          IO { println(s"[LOGGER] Error: ${e.getMessage}") } *> IO.pure(
            List.empty[String]
          )
      }
  }

  def fetchPage(pageURL: String): Resource[IO, Document] = {
    Resource.make(IO {
      println(s"[LOGGER] Estabilishing connection on $pageURL")
    }
      *> IO { Jsoup.connect(pageURL).get() })(_ =>
      IO { println(s"[LOGGER] Closed connection on $pageURL") }
    )
  }

}
