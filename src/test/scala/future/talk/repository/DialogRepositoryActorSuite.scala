package future.talk.repository

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import future.talk.DialogGenerator
import DialogGenerator._
import future.talk.model.requests.{DialogCreateRequest, TalkRequest}
import future.talk.model.{Dialog, Talk}
import org.scalacheck.Prop._
import org.scalatest.FunSuiteLike
import org.scalatest.prop._

import scala.concurrent.duration._

class DialogRepositoryActorSuite(_system: ActorSystem) extends TestKit(_system) with FunSuiteLike with Checkers with ImplicitSender {

  def this() = this(ActorSystem("future-talk-app-test"))

  test("should save dialog into search engine") {
    val repository = system.actorOf(Props[DialogRepositoryActor])
    check(forAll(dialogGen) {
      dialog =>
        repository ! DialogCreateRequest(dialog.topic, dialog.talks.map(l => l.map(t=> TalkRequest(t.content, t.person, t.time))))
        expectMsgPF(5 minutes) {
          case CREATED(actual) => verifyDialog(dialog, actual)
          case _ => false
        }
    })
  }

  def verifyDialog(dialog: Dialog, savedDialog: Dialog): Boolean = {
    val isTopicValid = savedDialog.topic == dialog.topic
    val isTalksValid = dialog.talks match {
      case Some(talks) => savedDialog.talks match {
        case Some(savedTalks) => verifyTalk(talks, savedTalks)
        case None => false
      }
      case None => savedDialog.talks == None
    }
    isTopicValid && isTalksValid
  }

  def verifyTalk(talks: List[Talk], savedTalks: List[Talk]) = {
    talks.forall(talk => savedTalks.exists(t => t.content == talk.content))
  }
}
