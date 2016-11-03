package monitoring

import akka.actor.{Actor, ActorRef, Terminated}

/**
  * Created by sankova on 11/2/16.
  */
class Ares(athena: ActorRef) extends Actor {
  override def preStart() = {
    println("Ares preStart... context watch Athena")
    context.watch(athena)
  }

  override def postStop() = {
    println("Ares postStop...")
  }

  def receive = {
    case Terminated =>
      context.stop(self)
  }
}
