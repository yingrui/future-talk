package future.talk.service

import java.io.File
import java.util.UUID

import akka.actor.{Actor, ActorPath, ActorRef, Props}
import future.talk.FutureTalkSettings
import future.talk.model.Dialog
import future.talk.model.dto.DialogIndexer
import future.talk.model.requests.GetStatus
import future.talk.util.{Guid, MyActors}
import org.json4s._
import org.json4s.native.JsonMethods._
import future.talk.MyJson4sFormat.formats

class Indexer(val id: UUID, val files: List[String], val indexActor: ActorRef) extends Actor {

  var dialogs = loadDialogs
  sendDialog

  def receive = {
    case GetStatus(_) => sender ! DialogIndexer(files, id.toString)
    case _ =>
  }

  def loadDialogs = {
    val dialogLists = for (file <- files; dialogList = parse(new File(file)).extract[List[Dialog]]) yield dialogList
    dialogLists.flatten
  }

  def sendDialog {
    indexActor ! dialogs.head
  }
}

object Indexer {

  def createIndexer(files: List[String]) = {
    val id = Guid.newId
    MyActors.system.actorOf(Props(classOf[Indexer], id, files, MyActors.dialogReposityActor), id.toString)
    id
  }

  def get(id: UUID) = {
    val path: ActorPath = MyActors.system / id.toString
    println(s"get indexer: ${path.toString}")
    MyActors.system.actorSelection(path)
  }

  def toUri(id: UUID) = spray.http.Uri(s"${FutureTalkSettings.rootPath}/indexer/${id}")
}
