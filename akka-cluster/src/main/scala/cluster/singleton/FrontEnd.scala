package cluster.singleton

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.singleton.{ClusterSingletonProxy, ClusterSingletonProxySettings}

import scala.concurrent.duration._

/**
  * Created by sankova on 11/15/16.
  */
class FrontEnd extends Actor with ActorLogging {
  import FrontEnd._
  import context.dispatcher

  val masterProxy = context.actorOf(ClusterSingletonProxy.props(
    singletonManagerPath = "/user/master",
    settings = ClusterSingletonProxySettings(context.system).withRole(None)
  ), name = "masterProxy")

  context.system.scheduler.schedule(0.second, 3.second, self, Tick)

  def receive = {
    case Tick =>
      masterProxy ! Master.Work(self, "add")
  }

}

object FrontEnd {
  case object Tick

  def props = Props(new FrontEnd())
}