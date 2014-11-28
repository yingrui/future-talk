package future.talk

import spray.http.StatusCodes
import spray.routing.HttpService
import StatusCodes._
import spray.httpx.marshalling.Marshaller

trait StaticResource extends HttpService {

  val dir = FutureTalkSettings.staticResourceDir

  val staticRoute = {
    pathSingleSlash {
      getFromFile(s"$dir/index.htm")
    } ~
    pathPrefix("css") {
      getFromDirectory(s"$dir/css")
    } ~
    pathPrefix("js") {
      getFromDirectory(s"$dir/js")
    } ~
    pathPrefix("fonts") {
      getFromDirectory(s"$dir/fonts")
    } ~
    pathPrefix("images") {
      getFromDirectory(s"$dir/images")
    } ~
    requestUri { uri =>
      if(uri.path.toString().endsWith(".htm"))
        getFromDirectory(dir)
      else
        complete(NotFound)
    }
  }

}
