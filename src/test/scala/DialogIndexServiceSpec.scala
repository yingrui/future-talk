import future.talk.DialogIndexService
import org.specs2.mutable._
import spray.http.HttpHeaders.Location
import spray.http.MediaTypes.`application/json`
import spray.http.HttpCharsets.`UTF-8`
import spray.http.{StatusCodes, ContentType, HttpEntity}
import spray.testkit._

class DialogIndexServiceSpec extends Specification with Specs2RouteTest with DialogIndexService {
  def actorRefFactory = system

  val jsonTalks = """[{"content":"Hi", "person": "Rob", "time":"2014/01/01 00:00:00"}]"""
  val jsonDialog = s"""{"topic":"greeting", "talks": $jsonTalks}"""

  "The Dialog Index Service" should {

    "return 201 when create dialog" in {
      Post("/dialogs", HttpEntity(ContentType(`application/json`, `UTF-8`), jsonDialog)) ~> indexRoute ~> check {
        response.header[Location] match {
          case Some(Location(uri)) => ok
          case None => failure
        }
        response.status === StatusCodes.Created
      }
    }
  }
}
