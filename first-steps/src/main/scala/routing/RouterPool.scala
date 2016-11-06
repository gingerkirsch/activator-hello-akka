package routing

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import routing.Worker.Work

/**
  * Created by sankova on 11/6/16.
  */
class RouterPool(paths: List[String]) extends Actor {
  var routees: List[ActorRef] = _
  override def preStart() = {
    routees = List.fill(5)(
      context.actorOf(Props[Worker])
    )
  }

  def receive() = {
    case msg: Work =>
      println("Router has just received a message...")
      routees(util.Random.nextInt(routees.size)) forward msg
  }
}

object RouterApp extends App {
  val system = ActorSystem("routing")
  system.actorOf(Props[Worker], "w1")
  system.actorOf(Props[Worker], "w2")
  system.actorOf(Props[Worker], "w3")
  system.actorOf(Props[Worker], "w4")
  system.actorOf(Props[Worker], "w5")

  val workers: List[String] = List(
    "/user/w1",
    "/user/w2",
    "/user/w3",
    "/user/w4",
    "/user/w5"
  )

  val routerGroup = system.actorOf(Props(classOf[RouterPool], workers))

  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()

  Thread.sleep(100)

  system.terminate()
}