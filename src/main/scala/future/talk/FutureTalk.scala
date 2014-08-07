package future.talk

import akka.actor.Actor

class FutureTalk extends Actor with DialogResource with HealthCheckResource with StaticResource with IndexerResource {

  def actorRefFactory = context

  implicit def json4sFormats = MyJson4sFormat.json4sFormats

  def receive = runRoute(indexRoute ~ indexerRoute ~ healthCheckRoute ~ staticRoute)
}