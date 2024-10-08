// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
import org.jsoup.nodes.Document

import munit.CatsEffectSuite

import cats.effect.{IO, SyncIO}

class ScraperTest extends munit.CatsEffectSuite {
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
      val expectedHtml =
        "<h1><a href=\"/\" title=\"Return to the main page\">Arch Linux</a></h1>"
      IO {
        assert(title.nonEmpty, "An h1 element should be found")
        assertEquals(title.head.toString, expectedHtml)
      }
    }
  }

  test("selectElementstext") {
    Scraper.fetchPage("https://archlinux.org/").use { doc =>
      val titleText =
        Scraper.selectElementsText(Scraper.selectDocElements(doc, "h1"), "h1")
      val expectedText = "Arch Linux"
      IO {
        assert(titleText.nonEmpty, "An h1 element should be found")
        assertEquals(titleText.head, expectedText)
      }
    }
  }

}
