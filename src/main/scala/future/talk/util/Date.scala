package future.talk.util

import spray.http.DateTime

object Date {

  implicit class RichString(dateStr: String) {
    def toDate = DateTime.fromIsoDateTimeString(dateStr)

    def toTicks = dateStr.toDate.get.clicks
  }

  def fromLong(ticks: Long): String = DateTime(ticks).toIsoDateTimeString
}
