package paths

import akka.actor.{ActorSystem, PoisonPill, Props}

/**
  * Created by sankova on 11/5/16.
  */
object ActorPath extends App{
  val system = ActorSystem("actor-paths")
  val counter1 = system.actorOf(Props[Counter], "counter")
  println(s"Actor Reference for counter1: $counter1")
  val counterSelection1 = system.actorSelection("counter")
  println(s"Actor Selection for count1 is: $counterSelection1")
  counter1 ! PoisonPill
  Thread.sleep(100)
  val counter2 = system.actorOf(Props[Counter], "counter")
  println(s"Actor Reference for count2 is: $counter2")
  val counterSelection2 = system.actorSelection("counter")
  println(s"Actor Selection for count2 is: $counterSelection2")
  system.terminate()
}
