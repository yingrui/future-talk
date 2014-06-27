package future.talk.repository

import akka.actor.Actor
import future.talk.model._
import future.talk.model.requests.{DialogCreateRequest, TalkRequest}
import future.talk.util.Guid

case class CREATE(dialog: Dialog)
case class CREATED(dialog: Dialog)

class DialogRepositoryActor extends Actor {

  val repository = new DialogRepository()

  def receive = {
    case DialogCreateRequest(topic: String, talks: Option[List[TalkRequest]]) => sender ! CREATED(Dialog(topic, talks.map(l => l.map(t =>Talk(t.content, t.person, t.time, Guid.newId))), Guid.newId))
    case _ =>
  }

}
