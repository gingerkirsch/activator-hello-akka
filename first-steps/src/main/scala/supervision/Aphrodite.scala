package supervision

import akka.actor.{Actor, OriginalRestartException}
import com.sun.org.apache.xml.internal.utils.StopParseException
import supervision.Aphrodite.{RestartException, ResumeException, StopException}

/**
  * Created by sankova on 11/2/16.
  */
object Aphrodite {
  case object ResumeException extends Exception
  case object StopException extends Exception
  case object RestartException extends Exception
}

class Aphrodite extends Actor {
  override def preRestart(reason: Throwable, message: Option[Any]) = {
    println("Aphrodite preStart hook...")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable) = {
    println("Aphrodite postRestart hook...")
    super.postRestart(reason)
  }

  override def preStart() = {
    println("Aphrodite preStart...")
    super.preStart()
  }


  override def postStop() = {
    println("Aphrodite postStop...")
    super.postStop()
  }

  def receive = {
    case "Resume" =>
      throw ResumeException
    case "Stop" =>
      throw StopException
    case "Restart" =>
      throw RestartException
    case _ =>
      throw new Exception
  }
}
