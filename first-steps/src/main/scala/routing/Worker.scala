package routing

import akka.actor.Actor
import routing.Worker.Work

/**
  * Created by sankova on 11/5/16.
  */
class Worker extends Actor {
  def receive = {
    case msg: Work =>
      println(s"You see me doin' work work work work work work.. P.S. my ActorRef is $self")
  }
}

object Worker {
  case class Work()
}
