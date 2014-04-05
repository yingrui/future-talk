package future.talk.model

import future.talk.FutureTalkSettings
import future.talk.model.dto.{TalkDto, DialogDto}
import future.talk.CustomImplicitConverter._

case class Talk(content: String, person: String, time: String, id: Int = 0)

case class Dialog(topic: String, talks: Option[List[Talk]], id: Int = 0)

object Talk {

  implicit class RichTalk(talk: Talk) {

    def toUri = spray.http.Uri(s"${FutureTalkSettings.rootPath}/talks/${talk.id}")

    def toDto = TalkDto(talk.content, talk.person, talk.time, talk.toUri)
  }

}

object Dialog {

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

  }

}