package future.talk

import java.util.concurrent.TimeUnit

import akka.actor.Props
import future.talk.repository.DialogRepositoryActor
import future.talk.util.{Guid, FileUtil, MyActors}
import org.specs2.mutable._
import spray.http.HttpCharsets.`UTF-8`
import spray.http.HttpHeaders.Location
import spray.http.MediaTypes.`application/json`
import spray.http.{ContentType, HttpEntity, StatusCodes}
import spray.testkit._

import scala.concurrent.duration._

class DialogImportSpec extends Specification with Specs2RouteTest with IndexerResource {
  def actorRefFactory = system

  implicit val timeout = RouteTestTimeout(FiniteDuration(1, TimeUnit.MINUTES))

  val file = FileUtil.createTempFile(
    """[{
      |"id":"34A39666-D5BD-42D7-AD3D-3D548337875D",
      |"topic":"greeting",
      |"talks": [{"id": "B883DAB4-72DF-4E25-9DD4-86C0638BAA70","content":"Hi", "person": "Rob", "time":"2014-01-01T00:00:00"}]}]""".stripMargin
  ).getAbsolutePath

  val importFiles = s"""["$file"]"""

  "The Dialog Indexer Resource" should {
    "return 203 and location when create dialog indexer" in {
      Post("/indexer", HttpEntity(ContentType(`application/json`, `UTF-8`), importFiles)) ~> indexerRoute ~> check {
        response.status === StatusCodes.Accepted
        response.header[Location] match {
          case Some(Location(uri)) => {
            Get(uri) ~> indexerRoute ~> check {
              response.status === StatusCodes.OK
              println(responseAs[String])
            }
          }
          case None => failure("should return location of created resource")
        }
      }
    }
  }

}
