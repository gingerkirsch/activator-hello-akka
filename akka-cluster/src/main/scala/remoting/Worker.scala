package remoting

import akka.actor.Actor
import remoting.Worker.Work

/**
  * Created by sankova on 11/7/16.
  */
class Worker extends Actor{
  def receive = {
  case msg: Work =>
    println(s"You see me doing work work work work work work.. My ActorRef is $self na na na na na na")
  }
}

object Worker {
  case class Work(message: String)
}