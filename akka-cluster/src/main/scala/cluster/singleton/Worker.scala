package cluster.singleton

import akka.actor.{ Actor, Props, ActorLogging}
import akka.cluster.singleton.{ClusterSingletonProxy, ClusterSingletonProxySettings}
import scala.concurrent.duration._

/**
  * Created by sankova on 11/15/16.
  */
class Worker extends Actor with ActorLogging {
  import Master._
  import context.dispatcher

  val masterProxy = context.actorOf(ClusterSingletonProxy.props(
    singletonManagerPath = "/user/master",
    settings = ClusterSingletonProxySettings(context.system).withRole(None)
  ), name = "masterProxy")

  context.system.scheduler.schedule(0.seconds, 30.seconds, masterProxy, RegisterWorker(self))
  context.system.scheduler.schedule(3.seconds, 3.seconds, masterProxy, RequestWork(self))

  def receive = {
    case Work(requester, op) =>
      log.info(s"Worker: received Work(op: $op, requester: $requester).")
  }
}

object Worker{
  def props = Props(new Worker)
}
