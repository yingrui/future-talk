package future.talk.service

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestProbe, ImplicitSender, TestKit}
import future.talk.MyJson4sFormat
import future.talk.model.{Talk, Dialog}
import future.talk.repository.CREATED
import future.talk.util.{FileUtil, Guid, MyActors}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import org.json4s.native.Serialization.write
import MyJson4sFormat.formats

class IndexerActorSuite(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(MyActors.system)

  val dialog1 = Dialog("greeting", Some(List[Talk](Talk("Hi", "Rob", "2014-01-01T00:00:00", Guid.newId))), Guid.newId)
  val dialog2 = Dialog("asking", Some(List[Talk](Talk("Hi", "Rob", "2014-02-01T00:00:00", Guid.newId))), Guid.newId)

  val file = FileUtil.createTempFile(write(List(dialog1, dialog2))).getAbsolutePath

  "An Indexer Actor" should {
    "should parse index file and index dialogs" in {
      val files = List(file)
      val indexerId = Guid.newId
      val probe = TestProbe()
      val indexActor = _system.actorOf(Props(classOf[Indexer], indexerId, files, probe.ref))

      probe.watch(indexActor)
      probe.expectMsg(dialog1)
      indexActor ! CREATED(dialog1)
      probe.expectMsg(dialog2)
      indexActor ! CREATED(dialog2)

      probe.expectTerminated(indexActor)
    }
  }

}
