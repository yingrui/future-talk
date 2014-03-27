package future.talk

object FutureTalkSettings {
  var schema = "http"
  var host = "localhost"
  var port = 8080

  def rootPath = s"${schema}://${host}:${port}"
}