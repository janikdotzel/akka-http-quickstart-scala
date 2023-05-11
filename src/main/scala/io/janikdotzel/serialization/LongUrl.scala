package io.janikdotzel.serialization

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{path, _}
import com.typesafe.config.ConfigFactory

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration.DurationInt

object LongUrl extends App {

  val OneThousandChars = "X" * 1000
  val ThreeThousandChars = "X" * 3000
  val TenThousandChars = "X" * 10000

//  val system = ActorSystem(Behaviors.empty, "system")
//  implicit val executionContext = system.executionContext

  implicit val system: ActorSystem = ActorSystem("system", ConfigFactory.load())
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

//  println(system.settings.config.getString(""))

  Http().newServerAt("localhost", 8080).bind(route)
  println("Server now online at http://localhost:8080/")


  def route() =
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
}


