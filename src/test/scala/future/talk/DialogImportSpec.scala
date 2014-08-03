package future.talk

import java.util.concurrent.TimeUnit

import future.talk.model.{Talk, Dialog}
import future.talk.util._
import org.specs2.mutable._
import org.junit.Assert._
import spray.http.HttpCharsets.`UTF-8`
import spray.http.HttpHeaders.Location
import spray.http.MediaTypes.`application/json`
import spray.http.{ContentType, HttpEntity, StatusCodes}
import spray.testkit._

import scala.concurrent.duration._

import org.json4s.native.Serialization
import MyJson4sFormat.formats

class DialogImportSpec extends Specification with Specs2RouteTest with IndexerResource {
  def actorRefFactory = system

  implicit val timeout = RouteTestTimeout(FiniteDuration(1, TimeUnit.MINUTES))

  val dialog1 = Dialog("greeting", Some(List[Talk](Talk("Hi", "Rob", "2014-01-01T00:00:00", Guid.newId))), Guid.newId)

  val file = FileUtil.createTempFile(Serialization.write(List(dialog1))).getAbsolutePath

  val importFiles = s"""["$file"]"""

  "The Dialog Indexer Resource" should {
    "return 203 and location when create dialog indexer" in {
      Post("/indexer", HttpEntity(ContentType(`application/json`, `UTF-8`), importFiles)) ~> indexerRoute ~> check {
        response.status === StatusCodes.Accepted
        response.header[Location] match {
          case Some(Location(uri)) => {
            Get(uri) ~> indexerRoute ~> check {
              assertTrue(response.status == StatusCodes.OK || response.status == StatusCodes.NotFound)
            }
          }
          case None => failure("should return location of created resource")
        }
      }
    }
  }

}
