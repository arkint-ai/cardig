import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.collection.JavaConverters._

object WebScraper {

  def main(args: Array[String]): Unit = {
    // URL to scrape
    val url = "https://example.com"

    try {
      // Fetch the HTML document from the provided URL
      val document: Document = Jsoup.connect(url).get()

      // Extract and print the title of the page
      val title = document.title()
      println(s"Title: $title")

      // Extract all the links (anchor tags) from the page
      val links = document.select("a[href]")

      println(s"Found ${links.size()} links:")

      // Iterate through the links and print their text and URLs
      for (link <- links.asScala) {
        println(s"Text: ${link.text()}, URL: ${link.attr("abs:href")}")
      }

    } catch {
      case e: Exception =>
        println(s"An error occurred: ${e.getMessage}")
    }
  }
}

