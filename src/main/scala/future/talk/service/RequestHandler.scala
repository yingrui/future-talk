package future.talk.service

import akka.actor._
import future.talk.model.requests.RequestMessage
import future.talk.repository._
import future.talk.service.PerRequest.{WithPath, WithProps}
import org.json4s.DefaultFormats
import spray.http.HttpHeaders.Location
import spray.http.{HttpHeader, StatusCode, StatusCodes}
import spray.httpx.Json4sSupport
import spray.routing.RequestContext

trait PerRequest extends Actor with Json4sSupport {
  def ctx: RequestContext
  def message: RequestMessage

  val json4sFormats = DefaultFormats

  def receive = {
    case CREATED(dialog) => complete(StatusCodes.Created, "", List(Location(dialog.toUri)))
    case _ =>
  }

  def complete[T <: AnyRef](status: StatusCode, obj: T, headers: List[HttpHeader]) = {
    ctx.withHttpResponseHeadersMapped(headerArray => headers)
      .complete(status, obj)
    context.stop(self)
  }
}

object PerRequest {
  case class WithProps(ctx: RequestContext, props: Props, message: RequestMessage) extends PerRequest {
    val target = context.actorOf(props)
    target ! message
  }

  case class WithPath(ctx: RequestContext, path: String, message: RequestMessage) extends PerRequest {
    val target = context.actorSelection(path)
    target.tell(message, self)
  }
}

object RequestHandler {

  def process(actorRefFactory: ActorRefFactory, requestCtx: RequestContext, props: Props, message: RequestMessage) =
    actorRefFactory.actorOf(Props(classOf[WithProps], requestCtx, props, message))

  def process(actorRefFactory: ActorRefFactory, requestCtx: RequestContext, path: String, message: RequestMessage) =
    actorRefFactory.actorOf(Props(classOf[WithPath], requestCtx, path, message))
}
