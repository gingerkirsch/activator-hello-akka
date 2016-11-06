package routing

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import routing.Worker.Work

/**
  * Created by sankova on 11/5/16.
  */
class Router extends Actor {
  var routees: List[ActorRef] = _

  override def preStart() = {
    routees = List.fill(5)(
      context.actorOf(Props[Worker])
    )
  }

  def receive() = {
    case msg: Work =>
      println("Router has just received a message..")
      routees(util.Random.nextInt(routees.size)) forward msg
  }
}

object Router extends App {
  val system = ActorSystem("routing")
  val router = system.actorOf(Props[Router])
  router ! Work()
  router ! Work()
  router ! Work()
  Thread.sleep(100)
  system.terminate()
}