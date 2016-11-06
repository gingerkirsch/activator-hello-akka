package paths

import akka.actor.{Actor, ActorIdentity, ActorRef, ActorSystem, Identify, Props}

/**
  * Created by sankova on 11/5/16.
  */
class Watcher extends Actor {
  var counterRef: ActorRef = _
  val selection = context.actorSelection("/user/counter")
  selection ! Identify(None)
  def receive = {
    case ActorIdentity(_, Some(ref)) =>
      println(s"Actor Reference for counter is $ref")
    case ActorIdentity(_, None) =>
      println("Oooops...")
  }
}

object Watcher extends App {
  val system = ActorSystem("watch-actor-selection")
  val counter = system.actorOf(Props[Counter], "counter")
  val watcher = system.actorOf(Props[Watcher], "watcher")
  system.terminate()
}
