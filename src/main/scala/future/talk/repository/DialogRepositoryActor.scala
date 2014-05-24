package future.talk.repository

import akka.actor.Actor
import future.talk.model.Dialog
import java.util.UUID

case class CREATE(dialog: Dialog)
case class CREATED(id: UUID)

class DialogRepositoryActor extends Actor {

  val repository = new DialogRepository()

  def receive = {
    case CREATE(dialog) => sender ! CREATED(dialog.id)
    case _ =>
  }

}
