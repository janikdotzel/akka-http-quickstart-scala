package io.janikdotzel

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import akka.http.scaladsl.server.Directives._

class LongUrlSpec extends AnyWordSpec with Matchers with ScalaFutures with ScalatestRouteTest {

  lazy val testKit = ActorTestKit()

  implicit def typedSystem: ActorSystem[_] = testKit.system

  override def createActorSystem(): akka.actor.ActorSystem = testKit.system.classicSystem

  val OneThousandChars = "X" * 1000
  val ThreeThousandChars = "X" * 3000
  val TenThousandChars = "X" * 10000

  "The Http server" should {
    "Succeed for an URL with 10.000 characters" in {
      val request = HttpRequest(uri = "/one")

      request ~> route ~> check {
        status should ===(StatusCodes.OK)
        responseAs[String].shouldEqual("success")
      }
    }

    "Fail for an URL with 3.000 characters" in {
      val request = HttpRequest(uri = "/" + TenThousandChars)

      request ~> route ~> check {
        status should ===(StatusCodes.UriTooLong)
      }
    }
  }

  val route = {
    concat(
      path("") {
        get {
          complete("Server up and running.")
        }
      },
      path("one") { //OneThousandChars) {
        get {
          complete("success")
        }
      },
      path("three") { //ThreeThousandChars) {
        get {
          complete("success")
        }
      },
      path("ten") { //TenThousandChars) {
        get {
          complete("success")
        }
      }
    )
  }
}
