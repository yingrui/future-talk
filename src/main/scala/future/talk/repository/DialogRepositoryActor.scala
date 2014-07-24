package future.talk.repository

import akka.actor.Actor
import future.talk.model._
import future.talk.model.requests.{DialogCreateRequest, TalkRequest}
import future.talk.util.Guid

case class CREATED(dialog: Dialog)

class DialogRepositoryActor extends Actor {

  val repository = new DialogRepository()

  def receive = {
    case DialogCreateRequest(topic: String, talks: Option[List[TalkRequest]]) =>
      createDialog(Dialog(topic, talks.map(l => l.map(t => Talk(t.content, t.person, t.time, Guid.newId))), Guid.newId))
    case dialog: Dialog => createDialog(dialog)
    case _ =>
  }

  def createDialog(dialog: Dialog) {
    repository.create(dialog)
    sender ! CREATED(dialog)
  }
}