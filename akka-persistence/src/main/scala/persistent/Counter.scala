package persistent

import akka.actor.ActorLogging
import akka.persistence._

/**
  * Created by sankova on 11/6/16.
  */
object Counter{
  sealed trait Operation {
    val count: Int
  }

  case class Increment(override val count: Int) extends Operation
  case class Decrement(override val count: Int) extends Operation

  case class Cmd(op: Operation)
  case class Evt(op: Operation)

  case class State(count: Int)

}

class Counter extends PersistentActor with ActorLogging {
  import Counter._

  println("Starting ........................")

  // Persistent Identifier
  override def persistenceId = "counter-example"

  var state: State = State(count= 0)

  def updateState(evt: Evt): Unit = evt match {
    case Evt(Increment(count)) =>
      state = State(count = state.count + count)
      takeSnapshot
    case Evt(Decrement(count)) =>
      state = State(count = state.count - count)
      takeSnapshot
  }

  // Persistent receive on recovery mood
  val receiveRecover: Receive = {
    case evt: Evt =>
      println(s"Counter received ${evt} in recovering mode")
      updateState(evt)
    case SnapshotOffer(_, snapshot: State) =>
      println(s"Counter received a snapshot with data: ${snapshot} in recovering mode")
      state = snapshot
    case RecoveryCompleted =>
      println(s"Recovery Completed. Switching to receiving mode...")

  }

  // Persistent receive on normal mood
  val receiveCommand: Receive = {
    case cmd @ Cmd(op) =>
      println(s"Counter received ${cmd}")
      persist(Evt(op)) { evt =>
        updateState(evt)
      }

    case "print" =>
      println(s"The current state of the counter is ${state}")

    case SaveSnapshotSuccess(metadata) =>
      println(s"Saved snapshot successfully.")
    case SaveSnapshotFailure(metadata, reason) =>
      println(s"Save snapshot failed and failure is due to ${reason}")

  }

  def takeSnapshot = {
    if(state.count % 5 == 0){
      saveSnapshot(state)
    }
  }

  //  override def recovery = Recovery.none

}