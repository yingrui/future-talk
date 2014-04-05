package future.talk.util

import java.util.UUID

object Guid {

  val empty = UUID.fromString("00000000-0000-0000-0000-000000000000")

  def newId = UUID.randomUUID()
}
