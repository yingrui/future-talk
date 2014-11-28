package future.talk

import akka.actor._
import akka.io.IO
import future.talk.util.MyActors
import spray.routing.SimpleRoutingApp
import spray.can.Http

object Main extends App with SimpleRoutingApp {

  setupNetworkInterface()

  import MyActors.system

  val service = system.actorOf(Props[FutureTalk], "future-talk-service")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, FutureTalkSettings.host, port = FutureTalkSettings.port)

  private def setupNetworkInterface() {
    FutureTalkSettings.host = if(args.length >= 1) args(0) else "localhost"
    FutureTalkSettings.port = if(args.length >= 2) args(1).toInt else 8080
  }
}
