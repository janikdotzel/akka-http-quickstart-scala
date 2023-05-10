package io.janikdotzel.serialization

import spray.json._
import DefaultJsonProtocol._

object SprayExample extends App {
  final case class User(id: Int, name: String)
  implicit val userFormat: RootJsonFormat[User] = jsonFormat2(User.apply)

  // JSON string to Scala Class
  val json1 = """{"id": 1, "name": "John"}"""
  val jsonAst1 = JsonParser(json1)
  val user1 = jsonAst1.convertTo[User]
  println("Scala Class: \n" + user1)

  // Scala Class to JSON
  val user2 = User(1, "Peter")
  val jsonAst2 = user2.toJson
  val json2 = jsonAst2.prettyPrint
  println("JSON: \n" + json2)
}
