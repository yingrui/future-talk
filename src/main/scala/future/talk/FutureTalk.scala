package future.talk

import akka.actor.Actor
import spray.routing.Route
import future.talk.model.requests.RequestMessage

class FutureTalk extends Actor with DialogResource with HealthCheckResource {

  def actorRefFactory = context

  def receive = runRoute(indexRoute ~ healthCheckRoute)
}