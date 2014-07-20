package future.talk.model

import java.util.UUID

import akka.actor.{ActorPath, Actor, Props}
import future.talk.FutureTalkSettings
import future.talk.model.dto.DialogIndexer
import future.talk.model.requests.GetStatus
import future.talk.util.{Guid, MyActors}

class Indexer(val files: List[String], val id: UUID) extends Actor {

  def receive = {
    case GetStatus(_) => sender ! DialogIndexer(files, id.toString)
    case _ =>
  }
}

object Indexer {

  def createIndexer(files: List[String]) = {
    val id = Guid.newId
    MyActors.system.actorOf(Props(classOf[Indexer], files, id), id.toString)
    id
  }

  def get(id: UUID) = {
    val path: ActorPath = MyActors.system / id.toString
    println(s"get indexer: ${path.toString}")
    MyActors.system.actorSelection(path)
  }

  def toUri(id: UUID) = spray.http.Uri(s"${FutureTalkSettings.rootPath}/indexer/${id}")
}
