package future.talk

import akka.actor.{Actor, Props}
import future.talk.repository.DialogRepositoryActor
import future.talk.util.SingletonActorDictionary

class FutureTalk extends Actor with DialogResource with HealthCheckResource {

  def actorRefFactory = context

  SingletonActorDictionary(classOf[DialogRepositoryActor]) = actorRefFactory.actorOf(Props[DialogRepositoryActor]).path.toString

  def receive = runRoute(indexRoute ~ healthCheckRoute)
}