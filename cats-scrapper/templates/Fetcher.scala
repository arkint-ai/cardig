import cats.effect.Concurrent
import cats.implicits._
import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._
import org.http4s._
import org.http4s.implicits._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.circe._
import org.http4s.Method._

trait Fetcher[F[_]] {
  def get: F[Fetcher.Page]
}

object Fetcher {
  def apply[F[_]](implicit ev: Fetcher[F]): Fetcher[F] = ev

  final case class Page(page: String) extends AnyVal
  object Page {
    implicit val pageDecoder: Decoder[Page] = deriveDecoder[Page]
    implicit def pageEntityDecoder[F[_]: Concurrent]: EntityDecoder[F, Page] =
      jsonOf
    implicit val jokeEncoder: Encoder[Page] = deriveEncoder[Page]
    implicit def jokeEntityEncoder[F[_]]: EntityEncoder[F, Page] =
      jsonEncoderOf
  }

  final case class PageError(e: Throwable) extends RuntimeException

  def impl[F[_]: Concurrent](C: Client[F]): Fetcher[F] = new Fetcher[F] {
    val dsl = new Http4sClientDsl[F] {}
    import dsl._
    def get: F[Fetcher.Page] = {
      C.expect[Page](GET(uri"https://icanhazdadjoke.com/"))
        .adaptError { case t =>
          PageError(t)
        } // Prevent Client Json Decoding Failure Leaking
    }
  }
}
