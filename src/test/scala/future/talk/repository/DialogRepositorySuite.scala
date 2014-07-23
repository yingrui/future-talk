package future.talk.repository

import org.scalatest.FunSuiteLike
import org.scalatest.prop._
import org.scalacheck.Prop._
import future.talk.model.{Dialog, Talk}
import future.talk.data.DialogGenerator._
import future.talk.CustomImplicitConverter._

class DialogRepositorySuite extends FunSuiteLike with Checkers {

  test("should save dialog into search engine") {
    val repository = new DialogRepository()
    check(forAll(dialogGen){
      dialog =>
        val id = repository.create(dialog).id
        repository.getById(id) match {
          case Some(savedDialog) => verifyDialog(savedDialog, dialog)
          case _ => false
        }
    })
  }

  def verifyDialog(savedDialog: Dialog, dialog: Dialog): Boolean = {
    val isTopicValid = savedDialog.topic == dialog.topic
    val isTalksValid = dialog.talks match {
      case Some(talks) => savedDialog.talks match {
        case Some(savedTalks) => verifyTalk(talks, savedTalks)
        case None => false
      }
      case None => savedDialog.talks == None
    }
    if(!isTopicValid) {
      println("topic is not valid")
    }
    if(!isTalksValid) {
      println("talks are not valid")
    }
    isTopicValid && isTalksValid
  }

  def verifyTalk(talks: List[Talk], savedTalks: List[Talk]) = {
    val isValid = talks.forall(talk => savedTalks.exists(t => t.id == talk.id))
    if(!isValid) {
      println("talks are not valid")
    }
    isValid
  }
}
