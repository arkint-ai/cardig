import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.HttpStatusException

//import scala.concurrent.duration.DurationInt
import cats.effect.IO

object StandvirtualScraper extends Scraper {
  def selectUsedModelsTitles(doc: Document): List[String] = {
    def selectUsedModelsCards(doc: Document): List[Element] = {
      selectDocElements(doc, ".car-card-info")
    }
    selectElementsText(selectUsedModelsCards(doc), "h3")
  }

  def selectNewModelsTitles(doc: Document): List[String] = {
    def selectNewModelsArticles(doc: Document): List[Element] = {
      selectDocElements(doc, "article[data-id]")
    }
    def selectNewModelTitleFromArticle(article: Element): Option[String] = {
      Option(article.select("h1").first())
        .flatMap(h1 => Option(h1.select("a").first()))
        .map(_.text())
    }
    selectNewModelsArticles(doc).flatMap(article =>
      selectNewModelTitleFromArticle(article).toList
    )
  }

  // TODO: refactor the ifs outta here
  // 2 functions, one for new, one for used
  // new: ?page=_
  // old: &pag=_
  def scrapeProducts(
      baseURL: String,
      pageNumber: Int = 1
  ): IO[List[String]] = {

    val pageURL = baseURL + s"?page=${pageNumber}"

    fetchPage(pageURL)
      .use { doc =>
        val productTitles = selectNewModelsTitles(doc)
        for {
          // _ <- IO.sleep(1.second)
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
        // TODO: getting redirected by cloudflare
        // bypass waf, direcly connect to IP if possible
        // rotate proxies when no products are found and try again,
        // if no products then its actually EOF
        // else need to keep that proxy until safe behaviour for next scrappings
        case e: HttpStatusException =>
          logger.error(s"Error: ${e.getStatusCode}: ${e.getMessage}")
          IO.pure(List.empty[String])
        case e =>
          logger.error(s"Error: ${e.getMessage}")
          IO.pure(List.empty[String])
      }
  }

}
