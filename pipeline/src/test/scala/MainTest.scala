import org.http4s.ember.client.EmberClientBuilder
import org.http4s.client.Client
import cats.effect.{IO, IOApp, Resource}
import scala.io.Source
import scala.util.{Try, Success, Failure}

class MainTest extends munit.CatsEffectSuite {
  test(
    "FileOPs: readLines and printLines operations and initial file existance"
  ) {
    val filePath: String = "/opt/cardig/data/initial/brands.txt"
    FileOPs.printLines(filePath)
  }

}
