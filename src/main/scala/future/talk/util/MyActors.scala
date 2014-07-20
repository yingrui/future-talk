package future.talk.util

import akka.actor.{Props, ActorSelection, ActorSystem}
import future.talk.repository.DialogRepositoryActor

object MyActors {

  implicit val system = ActorSystem("future-talk-app")

  def actorSelection(path: String): ActorSelection = system.actorSelection(path)

  val dialogReposityActor = system.actorOf(Props[DialogRepositoryActor])

}
