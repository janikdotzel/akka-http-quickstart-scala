package io.janikdotzel.serialization

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{path, _}

object LongUrl extends App {

  val OneThousandChars = "X" * 1000
  val ThreeThousandChars = "X" * 3000
  val TenThousandChars = "X" * 10000

  implicit val system = ActorSystem(Behaviors.empty, "system")
  implicit val executionContext = system.executionContext

  Http().newServerAt("localhost", 8080).bind(route)
  println("Server now online at http://localhost:8080/")


  val route = concat(
    pathPrefix("") {
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

  def onTest(): String = {
    Http().singleRequest(HttpRequest(uri = "http://localhost:8080/" + OneThousandChars)).onComplete( res => println("1.000 response: " + res))
    Http().singleRequest(HttpRequest(uri = "http://localhost:8080/" + ThreeThousandChars)).onComplete( res => println("3.000 response: " + res))
    Http().singleRequest(HttpRequest(uri = "http://localhost:8080/" + TenThousandChars)).onComplete( res => println("10.000 response: " + res))

    "test requests were made"
  }
}


