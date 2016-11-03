package conversation

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import conversation.Checker.{BlackUser, CheckUser, WhiteUser}
import conversation.Recorder.NewUser
import conversation.Storage.AddUser
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sankova on 11/2/16.
  */
object Recorder {
  sealed trait RecorderMsg
  case class NewUser(user: User) extends RecorderMsg
  def props(checker: ActorRef, storage: ActorRef) = Props(new Recorder(checker, storage))
}

class Recorder(checker: ActorRef, storage: ActorRef) extends Actor {
  implicit val timeout = Timeout(5 seconds)

  def receive = {
    case NewUser(user) => {
      checker ? CheckUser(user) map {
        case WhiteUser(user) =>
          storage ! AddUser(user)
        case BlackUser(user) =>
          println(s"Recorder: $user is in the blacklist")
      }
    }
    case _ => println("unknown message")
  }
}
