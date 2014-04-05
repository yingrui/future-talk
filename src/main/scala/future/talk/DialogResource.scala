package future.talk

import spray.routing.HttpService
import future.talk.model._
import future.talk.model.requests._
import spray.http.StatusCodes
import spray.http.HttpHeaders.Location


trait DialogResource extends HttpService {

  import spray.httpx.SprayJsonSupport._
  import CustomJsonProtocol._

  val indexRoute = {
    path("dialogs") {
      post {
        entity(as[DialogCreateRequest]) {
          request => createDialog(request) match {
            case Some(location: Location) => respondWithHeader(location) {
              complete(StatusCodes.Created)
            }
            case _ => complete(StatusCodes.BadRequest)
          }
        }
      }
    } ~
    get {
      pathPrefix("dialogs" / IntNumber) { id =>
        complete(Dialog("greeting", Some(List(Talk("Hi", "Rob", "2014/01/01 00:00:00", 1))), id).toDto)
      }
    }
  }

  def createDialog(request: DialogCreateRequest): Option[Location] = {
    request match {
      case DialogCreateRequest(topic, Some(talks)) => {
        val dialog = create(topic, talks)
        Some(Location(dialog.toUri))
      }
      case _ => None
    }
  }

  def create(topic: String, talks: List[TalkRequest]): Dialog = {
    Dialog(topic, Some(talks.map(t => Talk(t.content, t.person, t.time, 1))), 1)
  }
}