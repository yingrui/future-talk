package future.talk

import spray.routing.HttpService
import future.talk.model._
import spray.http.{Uri, StatusCodes}
import spray.http.HttpHeaders.Location

import spray.httpx.SprayJsonSupport._
import CustomJsonProtocol._

trait DialogIndexService extends HttpService {

  val indexRoute = {
    path("dialogs") {
      post {
        entity(as[DialogCreateRequest]) {
          request =>
            request match {
              case DialogCreateRequest(topic, Some(talks)) => {
                val dialog = Dialog(topic, Some(talks), 1)
                respondWithHeader(Location(dialog.toUri)) {
                  complete (StatusCodes.Created)
                }
              }
              case _ => complete(StatusCodes.BadRequest)
            }
        }
      }
    }
  }
}