package future.talk

import akka.actor.Actor

class FutureTalk extends Actor with DialogResource with HealthCheckResource with StaticResource with IndexerResource {

  def actorRefFactory = context

  def receive = runRoute(indexRoute ~ healthCheckRoute ~ staticRoute ~ indexerRoute)
}