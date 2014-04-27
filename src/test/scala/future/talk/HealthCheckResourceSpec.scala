package future.talk

import org.specs2.mutable._
import spray.http.StatusCodes
import spray.testkit._

class HealthCheckResourceSpec extends Specification with Specs2RouteTest with HealthCheckResource {
  def actorRefFactory = system

  "The Health Check Resource" should {
    "return success when everything is ok" in {
      Get("/health") ~> healthCheckRoute ~> check {
        response.status === StatusCodes.OK
        responseAs[String] === "success"
      }
    }
  }
}
