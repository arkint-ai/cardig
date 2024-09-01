@main def hello(): Unit =
  println("Hello world!")
  println(msg)
 
  val url = "https://example.com"
  val doc = Scraper.scrapeWebsite(url)
  val title = Scraper.extractTitle(doc)
  println(s"Title: $title")

def msg = "I was compiled by Scala 3. :)"
