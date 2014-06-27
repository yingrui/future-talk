package future.talk.service

import akka.actor._
import future.talk.model.requests.RequestMessage
import spray.http.{HttpHeader, StatusCode}
import spray.routing.RequestContext
import spray.http.StatusCodes
import scala.concurrent.duration._
import future.talk.service.PerRequest.WithProps
import future.talk.repository._
import spray.http.HttpHeaders.Location
import future.talk.CustomJsonProtocol
import spray.httpx.Json4sSupport
import org.json4s.DefaultFormats

trait PerRequest extends Actor with Json4sSupport {
  def r: RequestContext
  def target: ActorRef
  def message: RequestMessage

  import context._

  val json4sFormats = DefaultFormats

  setReceiveTimeout(2.seconds)

  target ! message

  def receive = {
    case CREATED(dialog) => complete(StatusCodes.Created, "", List(Location(dialog.toUri)))
    case _ =>
  }

  def complete[T <: AnyRef](status: StatusCode, obj: T, headers: List[HttpHeader] = List()) = {
    r.withHttpResponseHeadersMapped(header => header:::headers)
    r.complete(status, obj)
    stop(self)
  }
}

object PerRequest {
  case class WithProps(r: RequestContext, props: Props, message: RequestMessage) extends PerRequest {
    lazy val target = context.actorOf(props)
  }
}


object RequestHandler {

  def process(actorRefFactory: ActorRefFactory, r: RequestContext, props: Props, message: RequestMessage) =
    actorRefFactory.actorOf(Props(classOf[WithProps], r, props, message))
}
