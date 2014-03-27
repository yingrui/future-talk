package future.talk

import akka.actor.Actor

class FutureTalk extends Actor with DialogIndexService {

  def actorRefFactory = context

  def receive = runRoute(indexRoute)
}

