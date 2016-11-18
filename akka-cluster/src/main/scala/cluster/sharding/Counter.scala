package cluster.sharding

import akka.actor.{ActorLogging, Props}
import akka.persistence.PersistentActor
import akka.persistence._

import scala.concurrent.duration._
import akka.cluster.sharding.ShardRegion

/**
  * Created by sankova on 11/18/16.
  */
class Counter extends PersistentActor with ActorLogging {
  import Counter._

  context.setReceiveTimeout(120.seconds)

  override def persistenceId: String = self.path.parent.name + "-" + self.path.name

  var count = 0

  def updateState(event: CounterChanged) = count += event.delta

  override def receiveRecover: Receive = {
    case event: CounterChanged => updateState(event)
  }

  override def receiveCommand: Receive = {
    case Increment =>
      log.info(s"Counter with path $self received Increment Command")
      persist(CounterChanged(+ 1))(updateState)
    case Decrement =>
      log.info(s"Counter with path $self received Decrement Command")
      persist(CounterChanged(- 1))(updateState)
    case Get =>
      log.info(s"Counter with path $self received Get Command")
      log.info(s"Count = $count")
      sender ! count
    case Stop =>
      context.stop(self)
  }
}


object Counter {
  trait Command
  case object Increment extends Command
  case object Decrement extends Command
  case object Get extends Command
  case object Stop extends Command

  trait Event
  case class CounterChanged(delta: Int) extends Event

  val shardName: String = "Counter"
  case class CounterMessage(id: Long, cmd: Command)

  val idExtractor: ShardRegion.ExtractEntityId = {
    case CounterMessage(id, msg) => (id.toString, msg)
  }

  val shardResolver: ShardRegion.ExtractShardId = {
    case CounterMessage(id, msg) => (id % 12).toString
  }

  def props() = Props[Counter]
}