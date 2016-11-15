package cluster.frontendbackend

import akka.cluster._

import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import cluster.{Add, BackEndRegistration}

/**
  * Created by sankova on 11/15/16.
  */
class FrontEnd extends Actor {
  var backEndNodes = IndexedSeq.empty[ActorRef]

  def receive = {
    case addOp: Add => {
      if (backEndNodes.isEmpty)
      println("Service is unavailable, no back-end node detected.")
      else {
        println(s"$backEndNodes")
        val selected = Random.nextInt(backEndNodes.size)
        println(s"Front-end: forwarding operation to back-end...Amount of nodes: ${backEndNodes.size}" +
          s"....Node selected: $selected")
        backEndNodes(selected) forward addOp
      }
    }
    case BackEndRegistration if !(backEndNodes.contains(sender)) =>
      backEndNodes = backEndNodes :+ sender()
      context watch(sender())
    case Terminated(a) =>
      backEndNodes = backEndNodes.filterNot(_ == a)
  }
}

object FrontEnd {
  private var _frontend: ActorRef = _

  def initiate() = {
    val config = ConfigFactory.load("frontendbackend").getConfig("FrontEnd")
    val system = ActorSystem("ClusterSystem", config)
    _frontend = system.actorOf(Props[FrontEnd], name = "frontend")
  }

  def getFrontend = _frontend
}
