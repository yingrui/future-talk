package future.talk.repository

import future.talk.util.Guid
import org.scalatest.FunSuiteLike
import org.scalatest.prop._
import org.scalacheck.Prop._
import future.talk.model.{Dialog, Talk}
import future.talk.data.DialogGenerator._
import future.talk.CustomImplicitConverter._
import org.junit.Assert._

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

  test("should throw exception when create duplicated dialog") {
    val repository = new DialogRepository()

    val talk = Talk("content", "person", "2000-01-01T00:00:00", Guid.newId)
    val dialog = Dialog("topic", Some(List(talk)), Guid.newId)
    repository.create(dialog)

    intercept[RuntimeException](repository.create(dialog))
  }

  test("should update dialog into search engine") {
    val repository = new DialogRepository()
    val talk = Talk("content", "person", "2000-01-01T00:00:00", Guid.newId)
    val talk2 = Talk("content2", "person2", "2000-01-02T00:00:00", Guid.newId)
    val dialogId = Guid.newId
    val dialog = Dialog("topic", Some(List(talk)), dialogId)
    repository.create(dialog)

    val newlyDialog = Dialog("topic", Some(List(talk, talk2)), dialogId)
    repository.update(newlyDialog)
    repository.getById(dialogId) match {
      case Some(updatedDialog) => assertTrue(verifyDialog(updatedDialog, newlyDialog))
    }
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
    val isValid = (talks.size == savedTalks.size) && talks.forall(talk => savedTalks.exists(t => t.id == talk.id))
    if(!isValid) {
      println("talks are not valid")
    }
    isValid
  }
}
