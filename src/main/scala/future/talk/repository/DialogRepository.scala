package future.talk.repository

import future.talk.model.{Talk, Dialog}
import java.util.UUID
import org.apache.lucene.document.{Field, StringField}
import org.apache.lucene.search.TermQuery
import org.apache.lucene.index.Term
import future.talk.util.Date._

class DialogRepository {

  def create(dialog: Dialog) = {
    SearchEngine.index(dialog.toDocument)
    dialog.talks match {
      case Some(talks) => talks.foreach(t => indexTalk(t, dialog))
      case None =>
    }
    dialog
  }

  def getById(id: UUID): Option[Dialog] = {
    SearchEngine.get(id) match {
      case Some(doc) => Some(Dialog(doc.get("topic"), getTalks(id), UUID.fromString(doc.get("id"))))
      case None => None
    }
  }

  private def getTalks(dialogId: UUID): Option[List[Talk]] = {
    val query = new TermQuery(new Term("dialogId", dialogId.toString))
    SearchEngine.search(query) match {
      case Some(docs) => Some(docs.map(doc => {
        Talk(doc.get("content"),
          doc.get("person"),
          fromLong(doc.getField("time").numericValue().longValue()),
          UUID.fromString(doc.get("id")))
      }))
      case None => None
    }
  }

  private def indexTalk(t: Talk, dialog: Dialog) {
    val talk = t.toDocument
    talk.add(new StringField("dialogId", dialog.id.toString, Field.Store.YES))
    SearchEngine.index(talk)
  }
}