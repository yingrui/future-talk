package future.talk.model.dto

case class TalkDto(content: String, person: String, time: String, uri: String)

case class DialogDto(topic: String, talks: Option[List[TalkDto]], uri: String)