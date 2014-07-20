package future.talk

import future.talk.model.requests._
import future.talk.repository.DialogRepository
import future.talk.service.RequestHandler._
import future.talk.util.MyActors
import spray.http.StatusCodes
import spray.routing.{HttpService, Route}

trait DialogResource extends HttpService {

  implicit def executionContext = actorRefFactory.dispatcher

  import spray.httpx.SprayJsonSupport._
  import CustomJsonProtocol._

  val indexRoute = {
    path("dialogs") {
      post {
        entity(as[DialogCreateRequest]) {
          handleRequest(_)
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

  def handleRequest(message: RequestMessage): Route =
    ctx => process(actorRefFactory, ctx, MyActors.dialogReposityActor, message)
}

