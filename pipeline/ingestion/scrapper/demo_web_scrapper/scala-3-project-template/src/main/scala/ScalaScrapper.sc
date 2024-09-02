// https://github.com/ruippeixotog/scala-scraper
//import $ivy.`org.jsoup:jsoup:1.15.4`
import $ivy.`net.ruippeixotog::scala-scraper:3.1.1`

import net.ruippeixotog.scalascraper.browser._
import net.ruippeixotog.scalascraper.model._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

val browser = JsoupBrowser()

//val doc = browser.get("http://en.wikipedia.org/")

val doc = browser.get("http://example.com")
doc >?> ("#footer")

