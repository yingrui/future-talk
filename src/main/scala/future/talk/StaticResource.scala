package future.talk

import spray.routing.HttpService
import spray.httpx.marshalling.Marshaller

trait StaticResource extends HttpService {

  val staticRoute = {
    pathSingleSlash {
      getFromResource("index.htm")
    }
  }

}
