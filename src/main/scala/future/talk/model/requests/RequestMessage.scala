package future.talk.model.requests

import java.util.UUID

sealed trait RequestMessage

case class TalkRequest(content: String, person: String, time: String) extends RequestMessage

case class DialogCreateRequest(topic: String, talks: Option[List[TalkRequest]]) extends RequestMessage

case class GetStatus(id: UUID) extends RequestMessage