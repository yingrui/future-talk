package future.talk

import akka.actor.Actor

class FutureTalk extends Actor with DialogResource {

  def actorRefFactory = context

  def receive = runRoute(indexRoute)
}

