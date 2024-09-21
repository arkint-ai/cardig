// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
import org.jsoup.nodes.Document

import cats.effect.IO

class ScraperTest extends munit.FunSuite {
  test("fetchPage connection") {
    Scraper.fetchPage("https://archlinux.org/").use { doc =>
      IO {
        assert(doc.isInstanceOf[Document], "Page should be a jsoup Document")
        assert(doc.title().nonEmpty, "Page should have a title")
      }
    }
  }

  test("selectDocElements") {
    Scraper.fetchPage("https://archlinux.org/").use { doc =>
      val title = Scraper.selectDocElements(doc, "h1")
      for {
        _ <- IO {
          assert(title.nonEmpty, "An h1 element should be found")
          val expectedHtml =
            "<h1><a href=\"/\" title=\"Return to the main page\">Arch Linux</a></h1>"
          assertEquals(title.head.toString.trim, expectedHtml.trim)
        }
      } yield ()
    }
  }

  test("selectElementstext") {
    Scraper.fetchPage("https://archlinux.org/").use { doc =>
      val titleText =
        Scraper.selectElementsText(Scraper.selectDocElements(doc, "h1"), "h1")
      for {
        _ <- IO {
          assert(titleText.nonEmpty, "An h1 element should be found")
          val expectedText = "Arch Linux"
          assertEquals(titleText.head, expectedText)
        }
      } yield ()
    }
  }

}
