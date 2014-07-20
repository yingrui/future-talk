package future.talk

import akka.actor._
import akka.io.IO
import future.talk.util.MyActors
import spray.routing.SimpleRoutingApp
import spray.can.Http

object Main extends App with SimpleRoutingApp {

  import MyActors.system

  val service = system.actorOf(Props[FutureTalk], "future-talk-service")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, FutureTalkSettings.host, port = FutureTalkSettings.port)
}
