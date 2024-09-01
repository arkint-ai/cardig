import $ivy.`org.jsoup:jsoup:1.15.4`
import org.jsoup._
import scala.jdk.CollectionConverters._

val url = "http://en.wikipedia.org/"
val doc = Jsoup.connect(url).get()

val title = doc.title()
println(s"Title: $title")

val inTheNews = doc.select("#mp-itn b a")
val onThisDay = doc.select("#mp-otd b a")
val didYouKnow = doc.select("#mp-dyk b a")

val otds = for(otd <- onThisDay.asScala) yield (otd.attr("title"), otd.attr("href"))
otds.foreach { case (title, href) =>
  println(s"Title: $title, Href: $href")
}

val headers = for (otd <- onThisDay.asScala) yield otd.text 
headers.foreach { case (header) =>
  println(s"Header: $header")
}
