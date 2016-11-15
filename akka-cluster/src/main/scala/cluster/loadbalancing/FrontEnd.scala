package cluster.loadbalancing

import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.cluster.Cluster
import akka.routing.FromConfig
import cluster.Add
import scala.util.Random

/**
  * Created by sankova on 11/15/16.
  */
class FrontEnd extends Actor {
  import context.dispatcher

  val backend = context.actorOf(FromConfig.props(), name = "backendRouter")

  context.system.scheduler.schedule(3.seconds, 3.seconds, self, Add(Random.nextInt(100), Random.nextInt(100)))

  def receive = {
    case addOp: Add =>
      println(s"Front-end: forwarding operation to back-end")
      backend forward addOp
  }
}


object FrontEnd {
  private var _frontend: ActorRef = _

  val upToN = 200

  def initiate() = {
    val config = ConfigFactory.parseString("akka.cluster.roles = [frontend]").
      withFallback(ConfigFactory.load("loadbalancer"))

    val system = ActorSystem("ClusterSystem", config)
    system.log.info("FrontEnd will start when there are 2 or more nodes in the BackEnd")
    Cluster(system) registerOnMemberUp {
      _frontend = system.actorOf(Props[FrontEnd],
        name = "frontend")
    }
  }

  def getFrontend = _frontend
}
