package future.talk.model.requests

case class TalkRequest(content: String, person: String, time: String)

case class DialogCreateRequest(topic: String, talks: Option[List[TalkRequest]])
