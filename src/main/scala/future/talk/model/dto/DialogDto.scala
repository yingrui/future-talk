package future.talk.model.dto

sealed trait ResponseMessage

case class TalkDto(content: String, person: String, time: String, uri: String) extends ResponseMessage

case class DialogDto(topic: String, talks: Option[List[TalkDto]], uri: String) extends ResponseMessage

case class DialogIndexer(files: List[String], id: String) extends ResponseMessage