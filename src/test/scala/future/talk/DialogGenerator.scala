package future.talk

import future.talk.model.{Dialog, Talk}
import future.talk.util.Guid
import org.scalacheck.Gen

object DialogGenerator {

  def talkGen: Gen[Talk] = Gen.resultOf[String, String, Talk](
    (content, person) => Talk(content, person, "2014-04-05T00:00:00", Guid.newId))

  def dialogGen: Gen[Dialog] = {
    val gen = for{
      topic <- Gen.alphaStr
      talks <- Gen.listOf1(talkGen)
    } yield (topic, talks)
    gen.flatMap[Dialog](t => Dialog(t._1, Some(t._2), Guid.newId))
  }

}
