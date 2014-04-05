package future.talk.repository

import org.scalatest.FunSuite
import org.scalatest.prop._
import org.scalacheck.Prop._
import org.scalacheck.Gen
import future.talk.model.{Dialog, Talk}
import future.talk.util.Guid

class DialogRepositorySuite extends FunSuite with Checkers {

  def talkGen: Gen[Talk] = Gen.resultOf[String, String, Talk](
    (content, person) => Talk(content, person, "2014/4/5"))

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
          case Some(savedDialog) => savedDialog.topic == dialog.topic
          case _ => false
        }
    })
  }
}
