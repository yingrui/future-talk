package future.talk

import org.specs2.mutable._
import spray.http.HttpHeaders.Location
import spray.http.MediaTypes.`application/json`
import spray.http.HttpCharsets.`UTF-8`
import spray.http.{StatusCodes, ContentType, HttpEntity}
import spray.json.JsonParser
import spray.testkit._
import future.talk.CustomJsonProtocol._
import future.talk.model.requests.RequestMessage
import spray.routing._
import future.talk.model.dto.DialogDto
import scala.Some

class DialogResourceSpec extends Specification with Specs2RouteTest with DialogResource {
  def actorRefFactory = system

  val jsonTalks = """[{"content":"Hi", "person": "Rob", "time":"2014-01-01T00:00:00"}]"""
  val jsonDialog = s"""{"topic":"greeting", "talks": $jsonTalks}"""

  "The Dialog Resource" should {
    "return 201 and location when create dialog" in {
      Post("/dialogs", HttpEntity(ContentType(`application/json`, `UTF-8`), jsonDialog)) ~> indexRoute ~> check {
        response.header[Location] match {
          case Some(Location(uri)) => ok
          case None => failure("should return location of created resource")
        }
        response.status === StatusCodes.Created
      }
    }

    "return dialog when get by uri" in {
      Post("/dialogs", HttpEntity(ContentType(`application/json`, `UTF-8`), jsonDialog)) ~> indexRoute ~> check {
        val location = response.header[Location].get.uri
        Get(location.path.toString()) ~> indexRoute ~> check {
          response.status === StatusCodes.OK
          val dialog = JsonParser(responseAs[String]).convertTo[DialogDto]
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
