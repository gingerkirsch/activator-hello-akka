package monitoring

import akka.actor.Actor

/**
  * Created by sankova on 11/2/16.
  */
class Athena extends Actor {
  def receive = {
    case msg =>
      println(s"Athena receives ${msg}")
      context.stop(self)
  }
}
