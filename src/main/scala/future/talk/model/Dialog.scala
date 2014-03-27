package future.talk.model

import future.talk.FutureTalkSettings

case class Talk (content: String, person: String, time: String)

case class Dialog (topic: String, talks: Option[List[Talk]], id: Long = 0)

case class DialogCreateRequest (topic: String, talks: Option[List[Talk]])


object Dialog {

  implicit class RichDialog(dialog: Dialog) {
    def toUri = {
      spray.http.Uri(s"${FutureTalkSettings.rootPath}/dialogs/${dialog.id}")
    }
  }

}