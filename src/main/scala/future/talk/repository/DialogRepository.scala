package future.talk.repository

import future.talk.model.Dialog
import java.util.UUID

class DialogRepository {

  def create(dialog: Dialog) = {
    SearchEngine.index(dialog.toDocument)
    dialog
  }

  def getById(id: UUID): Option[Dialog] = {
    SearchEngine.get(id) match {
      case Some(doc) => Some(Dialog(doc.get("topic"), None, UUID.fromString(doc.get("id"))))
      case None => None
    }
  }
}