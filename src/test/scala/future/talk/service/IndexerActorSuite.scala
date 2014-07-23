package future.talk.service

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import future.talk.MyJson4sFormat
import future.talk.model.{Talk, Dialog}
import future.talk.util.{FileUtil, Guid, MyActors}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import org.json4s.native.Serialization.write
import MyJson4sFormat.formats

class IndexerActorSuite(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(MyActors.system)

  val dialog = Dialog("greeting", Some(List[Talk](Talk("Hi", "Rob", "2014-01-01T00:00:00", Guid("B883DAB4-72DF-4E25-9DD4-86C0638BAA70")))), Guid("34A39666-D5BD-42D7-AD3D-3D548337875D"))

  val file = FileUtil.createTempFile(write(List(dialog))).getAbsolutePath

  "An Indexer Actor" should {
    "should parse index file and index dialogs" in {
      val files = List(file)
      val indexerId = Guid.newId
      val indexerActor = _system.actorOf(Props(classOf[Indexer], indexerId, files, self))

      expectMsg(dialog)
    }
  }

}
