package future.talk

import akka.actor.ActorSelection
import future.talk.model.Indexer
import future.talk.model.requests._
import future.talk.service.RequestHandler._
import spray.http.HttpHeaders.Location
import spray.http.StatusCodes
import spray.routing.{HttpService, Route}

trait IndexerResource extends HttpService {

  import future.talk.CustomJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  val indexerRoute = {
    path("indexer") {
      post {
        entity(as[List[String]]) { files =>
          val dialogIndexerId = Indexer.createIndexer(files)
          respondWithHeader(Location(Indexer.toUri(dialogIndexerId))) {
            complete(StatusCodes.Accepted)
          }
        }
      }
    } ~
    get {
      pathPrefix("indexer" / JavaUUID) { id =>
        handleRequest(GetStatus(id), Indexer.get(id))
      }
    }
  }

  def handleRequest(message: RequestMessage, target: ActorSelection): Route =
    ctx => process(actorRefFactory, ctx, target, message)
}