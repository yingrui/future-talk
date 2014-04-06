package future.talk.repository

import org.scalatest.FunSuite
import org.scalatest.prop._
import org.scalacheck.Prop._
import org.scalacheck.Gen
import future.talk.model.{Dialog, Talk}
import future.talk.util.Guid

class DialogRepositorySuite extends FunSuite with Checkers {

  def talkGen: Gen[Talk] = Gen.resultOf[String, String, Talk](
    (content, person) => Talk(content, person, "2014-04-05T00:00:00", Guid.newId))

  def dialogGen: Gen[Dialog] = {
    val gen = for{
      topic <- Gen.alphaStr
      talks <- Gen.listOf1(talkGen)
    } yield (topic, talks)
    gen.flatMap[Dialog](t => Dialog(t._1, Some(t._2), Guid.newId))
  }

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
