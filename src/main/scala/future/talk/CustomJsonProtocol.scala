package future.talk

import future.talk.model._
import spray.json.DefaultJsonProtocol

object CustomJsonProtocol extends DefaultJsonProtocol {

  implicit val talkFormat = jsonFormat3(Talk)

  implicit val dialogCreateRequestFormat = jsonFormat2(DialogCreateRequest)
}
