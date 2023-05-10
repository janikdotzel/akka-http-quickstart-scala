package io.janikdotzel.serialization

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import scala.concurrent.Future


object Main {
  final case class User(id: String, name: String, age: Int)
  final case class GetUserResponse(success: Boolean)
  final case class CreateUserResponse(success: Boolean)

  object JsonFormats {
    // import the default encoders for primitive types (Int, String, Lists etc)
    import DefaultJsonProtocol._

    // Create Json Formats for every case class that is used in requests and responses
    implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat3(User.apply)
    implicit val getUserResponse: RootJsonFormat[GetUserResponse] = jsonFormat1(GetUserResponse.apply)
    implicit val createUserResponse: RootJsonFormat[CreateUserResponse] = jsonFormat1(CreateUserResponse.apply)
  }


  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "system")
    implicit val executionContext = system.executionContext

    Http().newServerAt("localhost", 8080).bind(routes())
    println(s"Server now online at http://localhost:8080/")

  }

  private def getUser(id: String): Future[GetUserResponse] = Future.successful(GetUserResponse(true))

  private def createUser(user: User): Future[CreateUserResponse] = Future.successful(CreateUserResponse(true))

  private def routes() = {
    // Needed to tell Akka-HTTP to use the Spray Json Support
    import io.janikdotzel.serialization.Main.JsonFormats._
    import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

    val route = pathPrefix("user") {
      path(Segment) { id =>
        concat(
          get {
            onSuccess(getUser(id)) { response => complete(response) }
          },
          post {
            // Marshalling works out-of-the-box through the `as[...]` directive
            entity(as[User]) { user => complete(createUser(user)) }
          })
      }
    }
    route
  }
}



