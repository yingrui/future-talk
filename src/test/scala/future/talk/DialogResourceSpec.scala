package future.talk

import java.util.concurrent.TimeUnit

import future.talk.model.dto.DialogDto
import future.talk.model.requests.{TalkRequest, DialogCreateRequest}
import org.specs2.mutable._
import spray.http.HttpHeaders.Location
import spray.http.StatusCodes
import spray.testkit._

import scala.concurrent.duration._

class DialogResourceSpec extends Specification with Specs2RouteTest with DialogResource {
  def actorRefFactory = system

  implicit def json4sFormats = MyJson4sFormat.json4sFormats
  implicit val timeout = RouteTestTimeout(FiniteDuration(5, TimeUnit.MINUTES))

  val dialogCreateRequest = DialogCreateRequest("greeting", Some(List(TalkRequest("Hi", "Rob", "2014-01-01T00:00:00"))))

  "The Dialog Resource" should {
    "return 201 and location when create dialog" in {
      Post("/dialogs", dialogCreateRequest) ~> indexRoute ~> check {
        response.header[Location] match {
          case Some(Location(uri)) => ok
          case None => failure("should return location of created resource")
        }
        response.status === StatusCodes.Created
      }
    }

    "return dialog when get by uri" in {
      Post("/dialogs", dialogCreateRequest) ~> indexRoute ~> check {
        val location = response.header[Location].get.uri
        Get(location.path.toString()) ~> indexRoute ~> check {
          response.status === StatusCodes.OK
          val dialog = responseAs[DialogDto]
          dialog.uri === location.toString
          dialog.topic === "greeting"
          dialog.talks match {
            case Some(talks) => {
              val talk = talks(0)
              talk.content === "Hi"
              talk.person === "Rob"
              talk.time === "2014-01-01T00:00:00"
            }
            case None => failure("should return talks")
          }
        }
      }
    }
  }
}
