package cluster.frontendbackend

import akka.cluster._
import com.typesafe.config.ConfigFactory
import akka.cluster.ClusterEvent.MemberUp
import akka.actor.{Actor, ActorRef, ActorSystem, Props, RootActorPath}
import cluster.{Add, BackEndRegistration}

/**
  * Created by sankova on 11/15/16.
  */
class BackEnd extends Actor {
  val cluster = Cluster(context.system)

  //subscribe to cluster changes, MemberUp
  //re-subscribe when restart
  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])
  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case Add(num1, num2) =>
      println(s"Backend node with path: $self has just received an Add operation.")
    case MemberUp(member) =>
      if (member.hasRole("frontend")){
        context.actorSelection(RootActorPath(member.address) / "user" / "frontend") !
          BackEndRegistration
      }
  }
}

object BackEnd {
  def initiate(port: Int) = {
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.load("frontendbackend").getConfig("BackEnd"))

    val system = ActorSystem("ClusterSystem", config)

    val backEnd = system.actorOf(Props[BackEnd], name = "backend")
  }
}