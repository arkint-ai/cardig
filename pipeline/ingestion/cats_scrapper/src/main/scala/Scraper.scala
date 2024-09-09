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
    products.collect {
      case product if product.select("h3").text().nonEmpty => product.select("h3").text()
    }
  }
 
  def scrapeProducts(
      baseUrl: String,
      pageNumber: Int
  ): IO[List[String]] = {

    val pageValue: String = s"&pag=$pageNumber"
    val url: String       = baseUrl + pageValue
    val nextPageNumber = pageNumber + 1
   
    connection(url).use { doc => 
      for {
        products <- IO(fetchProducts(doc))
        productTitles <- IO(selectProductTitles(products))
        _ <- IO { println(s"[LOGGER] Found ${productTitles.size} products on page $pageNumber") }
        
        productsAcc <- if (productTitles.nonEmpty) {
          scrapeProducts(baseUrl, pageNumber + 1).map(nextPageProducts => productTitles ++ nextPageProducts)
        } else {
          IO.pure(List.empty[String])
        }
      } yield productsAcc
    }.handleErrorWith {
      case e: HttpStatusException => IO { println(s"[LOGGER] Error ${e.getStatusCode}: ${e.getMessage}") } >> IO.pure(List.empty[String]) 
      case e => IO { println(s"[LOGGER] Error: ${e.getMessage}") } >> IO.pure(List.empty[String]) 
    }
  }

  def connection(url: String): Resource[IO, org.jsoup.nodes.Document] =
    Resource.make(
      IO { println(s"[LOGGER] Estabilishing connection on $url") }
      .flatMap(_ => IO { Jsoup.connect(url).get() } )
    )(_ => IO { println(s"[LOGGER] Closed connection on $url") } )

}
