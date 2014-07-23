package future.talk.util

import java.util.UUID

import org.json4s._
import org.json4s.JsonAST.JString

object Guid {

  val empty = UUID.fromString("00000000-0000-0000-0000-000000000000")

  def newId = UUID.randomUUID()

  def apply(guid: String): UUID = UUID.fromString(guid)
}

class GuidSerializer extends CustomSerializer[UUID](format => ({
  case JString(s) => Guid(s)
}, {
  case x: UUID => JString(x.toString)
}))
