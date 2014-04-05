package future.talk.repository

import future.talk.model.Dialog

class DialogRepository {

  def create(dialog: Dialog) = {
    Dialog(dialog.topic, dialog.talks, 1)
  }

  def getById(id: Int) = {
    Some(Dialog("", None, 1))
  }
}
