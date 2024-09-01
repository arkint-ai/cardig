package scrapper.demo_web_scrapper


final class DemoScrapper$u002Eworksheet$_ {
def args = DemoScrapper$u002Eworksheet_sc.args$
def scriptPath = """scrapper/demo_web_scrapper/DemoScrapper.worksheet.sc"""
/*<script>*/
import $dep.`org.jsoup:jsoup:1.15.4`

import org.jsoup._
import org.jsoup.nodes.Document

// Define the URL to scrape
val url = "https://example.com"

// Fetch the HTML document
val doc: Document = Jsoup.connect(url).get()

// Extract the title of the page
val title = doc.title()
println(s"Title: $title")

// Extract all paragraphs and print their text
val paragraphs = doc.select("p").eachText()
paragraphs.foreach(println)


/*</script>*/ /*<generated>*//*</generated>*/
}

object DemoScrapper$u002Eworksheet_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new DemoScrapper$u002Eworksheet$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export DemoScrapper$u002Eworksheet_sc.script as `DemoScrapper.worksheet`

