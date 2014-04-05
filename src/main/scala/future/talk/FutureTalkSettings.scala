package future.talk

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
}

object CustomJsonProtocol extends DefaultJsonProtocol {

  implicit val talkFormat = jsonFormat3(TalkRequest)
  implicit val dialogCreateRequestFormat = jsonFormat2(DialogCreateRequest)

  implicit val talkDtoFormat = jsonFormat4(TalkDto)
  implicit val dialogDtoFormat = jsonFormat3(DialogDto)
}
