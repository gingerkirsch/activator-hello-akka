package routing

import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig
import routing.Worker.Work

/**
  * Created by sankova on 11/6/16.
  */
object SmallestMailboxRouter  extends App {
  val system = ActorSystem("router")

  system.actorOf(Props[Worker], "w1")
  system.actorOf(Props[Worker], "w2")
  system.actorOf(Props[Worker], "w3")

  val paths = List("/user/w1", "/user/w2", "/user/w3")
  val routerGroup = system.actorOf(FromConfig.props(Props[Worker]), "smallest-mailbox-pool")

  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()

  system.terminate()
}
