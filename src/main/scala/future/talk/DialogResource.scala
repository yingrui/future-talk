package future.talk

import spray.routing.HttpService
import future.talk.model._
import future.talk.model.requests._
import spray.http.StatusCodes
import spray.http.HttpHeaders.Location
import future.talk.util.Guid
import future.talk.repository.DialogRepository


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
      pathPrefix("dialogs" / JavaUUID) { id =>
        new DialogRepository().getById(id) match {
          case Some(dialog) => complete(dialog.toDto)
          case None => complete(StatusCodes.NotFound)
        }
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
    new DialogRepository().create(Dialog(topic, Some(talks.map(t => Talk(t.content, t.person, t.time, Guid.newId))), Guid.newId))
  }
}