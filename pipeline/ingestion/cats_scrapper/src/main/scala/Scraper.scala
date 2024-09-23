import org.jsoup._
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.HttpStatusException

import scala.jdk.CollectionConverters._

import cats.effect.{IO, Resource}

import com.typesafe.scalalogging.LazyLogging

// TODO: Make URL a type not String
// https://github.com/lemonlabsuk/scala-uri

trait Scraper extends LazyLogging {
  // NOTE: tags can be HTML tags (like h1), classes (.something) and such
  def selectDocElements(doc: Document, tag: String): List[Element] = {
    doc.select(tag).asScala.toList
  }

  def selectElementsText(elements: List[Element], tag: String): List[String] = {
    elements
      .map { element =>
        val elementText = element.select(tag).text()
        if (elementText.nonEmpty) Some(elementText) else None
      }
      .collect { case Some(elementText) =>
        elementText
      }
  }

  def fetchPage(pageURL: String): Resource[IO, Document] = {
    // TODO: should this be async? How? Where?
    Resource.make {
      IO {
        logger.debug(s"Establishing connection on $pageURL")
        Jsoup.connect(pageURL).get()
      }
    } { _ =>
      IO {
        logger.debug(s"Closed connection on $pageURL")
      }
    }
  }

}

object Scraper extends Scraper

object StandvirtualScraper extends Scraper {
  
  def scrapeProducts(
      baseURL: String,
      pageNumber: Int = 1
  ): IO[List[String]] = {

    val pageURL: String             = baseURL + s"&pag=${pageNumber}"
    val titlesCardInfoClass: String = ".car-card-info"
    val titlesTextTag: String       = "h3"

    fetchPage(pageURL)
      .use { doc =>
        val productTitles = selectElementsText(
          selectDocElements(doc, titlesCardInfoClass),
          titlesTextTag
        )
        for {
          productsAcc <-
            if (productTitles.nonEmpty) {
              logger.info(s"Found ${productTitles.size} products on ${pageURL}")
              scrapeProducts(baseURL, pageNumber + 1)
                .map(nextPageProductTitles =>
                  productTitles ++ nextPageProductTitles
                )
            } else {
              IO.pure { List.empty[String] }
            }
        } yield productsAcc
      }
      .handleErrorWith {
        case e: HttpStatusException =>
          logger.error(s"Error: ${e.getStatusCode}: ${e.getMessage}")
          IO.pure(List.empty[String])
        case e =>
          logger.error(s"Error: ${e.getMessage}")
          IO.pure(List.empty[String])
      }
  }


}

