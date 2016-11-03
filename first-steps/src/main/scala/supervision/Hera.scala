package supervision

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props}
import akka.util.Timeout
import supervision.Aphrodite.{RestartException, ResumeException, StopException}

import scala.concurrent.duration._

/**
  * Created by sankova on 11/2/16.
  */
class Hera extends Actor {
  var childRef: ActorRef = _

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 second){
    case ResumeException => Resume
    case RestartException => Restart
    case StopException => Stop
    case _: Exception => Escalate
  }

  override def preStart() = {
    childRef = context.actorOf(Props[Aphrodite], "Aphrodite")
    Thread.sleep(100)
  }

  def receive = {
    case msg =>
      println(s"Hera received ${msg}")
      childRef ! msg
      Thread.sleep(100)
  }
}
