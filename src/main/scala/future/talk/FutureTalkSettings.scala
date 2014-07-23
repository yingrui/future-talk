package future.talk

import java.util.UUID

import future.talk.util.{GuidSerializer, Guid}
import org.json4s.DefaultFormats
import spray.http.Uri
import spray.json.DefaultJsonProtocol
import future.talk.model.requests.{DialogCreateRequest, TalkRequest}
import future.talk.model.dto.{DialogDto, TalkDto}

object FutureTalkSettings {
  var schema = "http"
  var host = "localhost"
  var port = 8080
  var dataPath = "data"

  def rootPath = s"${schema}://${host}:${port}"
}

object CustomImplicitConverter {

  implicit def uri2String(uri: Uri) = uri.toString()
  implicit def guid2String(guid: UUID) = guid.toString()
  implicit def string2Guid(guid: String) = Guid(guid)
}

object CustomJsonProtocol extends DefaultJsonProtocol {

  implicit val talkFormat = jsonFormat3(TalkRequest)
  implicit val dialogCreateRequestFormat = jsonFormat2(DialogCreateRequest)

  implicit val talkDtoFormat = jsonFormat4(TalkDto)
  implicit val dialogDtoFormat = jsonFormat3(DialogDto)
}

object MyJson4sFormat {
  implicit val formats = DefaultFormats + new GuidSerializer
}