package monitoring

import akka.actor.{ActorSystem, Props}

/**
  * Created by sankova on 11/2/16.
  */
object Monitoring extends App{
  val system = ActorSystem("monitoring")
  val athena = system.actorOf(Props[Athena], "athena")
  val ares = system.actorOf(Props(classOf[Ares], athena), "ares")

  athena ! "Hi"

  system.terminate()
}
