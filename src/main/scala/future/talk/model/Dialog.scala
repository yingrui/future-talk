package future.talk.model

import future.talk.FutureTalkSettings
import future.talk.CustomImplicitConverter._
import future.talk.util.Guid
import future.talk.util.Date._
import java.util.UUID
import org.apache.lucene.document._
import future.talk.model.dto.TalkDto
import future.talk.model.dto.DialogDto
import scala.Some

case class Talk(content: String, person: String, time: String, id: UUID = Guid.empty)

case class Dialog(topic: String, talks: Option[List[Talk]], id: UUID = Guid.empty)

object Talk {

  implicit class RichTalk(talk: Talk) {

    def toUri = spray.http.Uri(s"${FutureTalkSettings.rootPath}/talks/${talk.id}")

    def toDto = TalkDto(talk.content, talk.person, talk.time, talk.toUri)

    def toDocument = {
      val doc = new Document
      doc.add(new StringField("id", talk.id.toString, Field.Store.YES))
      doc.add(new StringField("person", talk.person, Field.Store.YES))
      doc.add(new LongField("time", talk.time.toTicks, Field.Store.YES))
      doc.add(new TextField("content", talk.content, Field.Store.YES))
      doc
    }
  }

}

object Dialog {

  def toUri(id: Int) {
    spray.http.Uri(s"${FutureTalkSettings.rootPath}/dialogs/${id}")
  }

  implicit class RichDialog(dialog: Dialog) {

    def toUri = spray.http.Uri(s"${FutureTalkSettings.rootPath}/dialogs/${dialog.id}")

    def toDto = DialogDto(
      dialog.topic,
      dialog.talks match {
        case Some(talks) => Some(talks.map(t => t.toDto))
        case None => None
      },
      dialog.toUri
    )

    def toDocument = {
      val doc = new Document
      doc.add(new StringField("id", dialog.id.toString, Field.Store.YES))
      doc.add(new TextField("topic", dialog.topic, Field.Store.YES))
      doc
    }
  }

}