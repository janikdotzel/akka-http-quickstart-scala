package io.janikdotzel.url

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{path, _}
import com.typesafe.config._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContextExecutor}

object LongUrl extends App {

  val OneThousandChars = "X" * 1000
  val ThreeThousandChars = "X" * 3000
  val TenThousandChars = "X" * 10000

  implicit val system: ActorSystem = ActorSystem("system", getConfig())
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  Http().newServerAt("localhost", 8080).bind(routes())
  println("Server now online at http://localhost:8080/")


  def routes() =
    concat(
      path("") {
        get {
          complete("Server up and running.")
        }
      },
      path("test") {
        get {
          complete(onTest())
        }
      },
      path(OneThousandChars) {
        get {
          complete("success")
        }
      },
      path(ThreeThousandChars) {
        get {
          complete("success")
        }
      },
      path(TenThousandChars) {
        get {
          complete("success")
        }
      }
    )

  private def onTest(): String = {
    val req1 = Http().singleRequest(HttpRequest(uri = "http://localhost:8080/" + OneThousandChars))
    val req3 = Http().singleRequest(HttpRequest(uri = "http://localhost:8080/" + ThreeThousandChars))
    val req10 = Http().singleRequest(HttpRequest(uri = "http://localhost:8080/" + TenThousandChars))

    Await.result(req1, 3.seconds) + "\n\n" + Await.result(req3, 3.seconds) + "\n\n" + Await.result(req10, 3.seconds)
  }

  private def getConfig(): Config = {

    // Load the application configuration (application.conf)
    val applicationConfig = ConfigFactory.load("application")

    // Get the library configuration
    val libraryConfig = LibraryConfig.fileBasedConfig

    // Merge the configurations, with library config having higher priority
    val finalConfig = libraryConfig.withFallback(applicationConfig)
    finalConfig
  }
}


