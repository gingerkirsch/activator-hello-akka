package cluster.loadbalancing

import akka.actor.{Actor, ActorSystem, Props}
import cluster.Add
import com.typesafe.config.ConfigFactory

/**
  * Created by sankova on 11/15/16.
  */
class BackEnd extends Actor {
  def receive = {
    case Add(num1, num2) =>
      println(s"Backend node with path: $self has just received an Add operation.")
  }
}

object BackEnd {
  def initiate(port: Int){
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]")).
      withFallback(ConfigFactory.load("loadbalancer"))
    val system = ActorSystem("ClusterSystem", config)

    val Backend = system.actorOf(Props[BackEnd], name = "backend")
  }
}