import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object Scraper {
  def scrapeWebsite(url: String): Document = {
    val doc = Jsoup.connect(url).get()
    doc
  }

  def extractTitle(doc: Document): String = {
    doc.title()
  }
}

