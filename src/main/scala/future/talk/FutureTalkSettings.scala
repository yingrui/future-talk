package future.talk

import java.io.{FileInputStream, File}
import java.util.{Properties, UUID}

import future.talk.util.{GuidSerializer, Guid}
import org.json4s.DefaultFormats
import spray.http.Uri
import spray.json.DefaultJsonProtocol
import scala.collection.JavaConverters._

object FutureTalkSettings {

  val configFile = new File("config.ini")

  val prop: collection.mutable.Map[String, String] = if (configFile.exists()) {
    val properties = new Properties()
    properties.load(new FileInputStream(configFile))
    properties.asScala
  } else {
    collection.mutable.Map[String, String]()
  }

  val schema = "http"
  val host = prop.getOrElse("host", "localhost")
  val port = prop.getOrElse("port", "8080").toInt
  val dataPath = prop.getOrElse("data.path", "data")

  val staticResourceDir = prop.getOrElse("static.resources", "./src/main/resources")

  val rootPath = s"${schema}://${host}:${port}"
}

object CustomImplicitConverter {

  implicit def uri2String(uri: Uri) = uri.toString()

  implicit def guid2String(guid: UUID) = guid.toString()

  implicit def string2Guid(guid: String) = Guid(guid)
}

object MyJson4sFormat {
  implicit val formats = DefaultFormats + new GuidSerializer
  implicit val json4sFormats = formats
}