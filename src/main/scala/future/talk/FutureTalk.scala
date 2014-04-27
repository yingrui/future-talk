package future.talk

import akka.actor.Actor

class FutureTalk extends Actor with DialogResource with HealthCheckResource {

  def actorRefFactory = context

  def receive = runRoute(indexRoute ~ healthCheckRoute)
}

