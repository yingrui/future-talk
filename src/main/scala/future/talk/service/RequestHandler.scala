package future.talk.service

import java.util.concurrent.TimeUnit

import akka.actor._
import future.talk.MyJson4sFormat
import future.talk.model.dto.ResponseMessage
import future.talk.model.requests.RequestMessage
import future.talk.repository._
import future.talk.service.PerRequest._
import org.json4s.DefaultFormats
import spray.http.HttpHeaders.Location
import spray.http.{HttpHeader, StatusCode, StatusCodes}
import spray.httpx.Json4sSupport
import spray.routing.RequestContext

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scala.concurrent.duration._

trait PerRequest extends Actor with Json4sSupport {
  def ctx: RequestContext
  def message: RequestMessage

  val json4sFormats = MyJson4sFormat.json4sFormats

  def receive = {
    case CREATED(dialog) => complete(StatusCodes.Created, "", List(Location(dialog.toUri)))
    case response: ResponseMessage => complete(StatusCodes.OK, response, List[HttpHeader]())
    case Terminated(_) => completeWith(StatusCodes.NotFound)
  }

  def completeWith(status: StatusCode) = complete(status, "", List[HttpHeader]())

  def complete[T <: AnyRef](status: StatusCode, obj: T, headers: List[HttpHeader]) = {
    ctx.withHttpResponseHeadersMapped(headerArray => headers ::: headerArray)
      .complete(status, obj)
    context.stop(self)
  }

  def initiate(target: ActorSelection) {
    target.resolveOne(1 minutes).andThen {
      case ref: ActorRef =>
        initiate(ref)
      case _ =>
        println("cannot resolve target actor")
        completeWith(StatusCodes.NotFound)
    }
  }

  def initiate(target: ActorRef) {
    context.watch(target)
    target ! message
  }
}

object PerRequest {

  case class WithProps(ctx: RequestContext, props: Props, message: RequestMessage) extends PerRequest {
    val target = context.actorOf(props)
    initiate(target)
  }

  case class WithActor(ctx: RequestContext, target: ActorRef, message: RequestMessage) extends PerRequest {
    initiate(target)
  }

  case class WithActorSelection(ctx: RequestContext, target: ActorSelection, message: RequestMessage) extends PerRequest {
    initiate(target)
  }

  case class WithPath(ctx: RequestContext, targetPath: String, message: RequestMessage) extends PerRequest {
    val target = context.actorSelection(targetPath)
    initiate(target)
  }
}

object RequestHandler {

  def process(actorRefFactory: ActorRefFactory, requestCtx: RequestContext, props: Props, message: RequestMessage) =
    actorRefFactory.actorOf(Props(classOf[WithProps], requestCtx, props, message))

  def process(actorRefFactory: ActorRefFactory, requestCtx: RequestContext, target: ActorRef, message: RequestMessage) =
    actorRefFactory.actorOf(Props(classOf[WithActor], requestCtx, target, message))

  def process(actorRefFactory: ActorRefFactory, requestCtx: RequestContext, target: ActorSelection, message: RequestMessage) =
    actorRefFactory.actorOf(Props(classOf[WithActorSelection], requestCtx, target, message))

  def process(actorRefFactory: ActorRefFactory, requestCtx: RequestContext, targetPath: String, message: RequestMessage) =
    actorRefFactory.actorOf(Props(classOf[WithPath], requestCtx, targetPath, message))
}
