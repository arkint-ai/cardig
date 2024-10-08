import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import akka.actor.{ActorSystem, Cancellable}
import scala.concurrent.Await
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.Behaviors

// https://typelevel.org/cats-effect/docs/2.x/datatypes/fiber
// https://typelevel.org/cats-effect/docs/2.x/concurrency/basics

object ScraperScheduler {

  def scraper1(): Unit = {
    println("Running Scraper 1...")
  }

  def scraper2(): Unit = {
    println("Running Scraper 2...")
  }

  def scraper3(): Unit = {
    println("Running Scraper 3...")
  }

  def run(): Unit = {
    val system: ActorSystem = ActorSystem("ScraperSystem")

    val scheduler1: Cancellable = system.scheduler.scheduleAtFixedRate(
      initialDelay = 4.seconds,
      interval = 4.seconds
    )(() => Future(scraper1()))

    val scheduler2: Cancellable = system.scheduler.scheduleAtFixedRate(
      initialDelay = 4.seconds,
      interval = 4.seconds
    )(() => Future(scraper2()))

    val scheduler3: Cancellable = system.scheduler.scheduleAtFixedRate(
      initialDelay = 4.seconds,
      interval = 4.seconds
    )(() => Future(scraper3()))

    Await.result(system.whenTerminated, Duration.Inf)

    scheduler1.cancel()
    scheduler2.cancel()
    scheduler3.cancel()
  }
}
