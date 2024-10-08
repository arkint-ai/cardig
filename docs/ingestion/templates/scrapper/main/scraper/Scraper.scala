import org.jsoup._
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

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
