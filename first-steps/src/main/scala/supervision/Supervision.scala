package supervision

import akka.actor.{ActorSystem, Props}

/**
  * Created by sankova on 11/2/16.
  */
object Supervision extends App {
  val system = ActorSystem("supervision")
  val hera = system.actorOf(Props[Hera], "hera")

  hera ! "Resume"
  Thread.sleep(100)
  println()

  hera ! "Restart"
  Thread.sleep(100)
  println()

  hera ! "Stop"
  Thread.sleep(100)
  println()
  system.terminate()
}
