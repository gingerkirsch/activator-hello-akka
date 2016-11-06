package behavior

import akka.actor.{Actor, ActorSystem, Props, Stash}
import behavior.UserStorage.{Connect, Disconnect, Operation}

/**
  * Created by sankova on 11/6/16.
  */
object UserStorage {
  trait DBOperation
  object DBOperation {
    case object Create extends DBOperation
    case object Read extends DBOperation
    case object Update extends DBOperation
    case object Delete extends DBOperation
  }

  case object Connect
  case object Disconnect
  case class Operation(dbOperation: DBOperation, user: Option[User])
}

class UserStorage extends Actor with Stash {

  def receive = disconnected

  def connected: Actor.Receive = {
    case Disconnect =>
      println(s"User Storage disconnected from DB")
    case Operation(op, user) =>
      println(s"User Storage receive $op to do in user: $user")
  }
  def disconnected: Actor.Receive = {
    case Connect =>
      println(s"User Storage connected to DB")
      unstashAll()
      context.become(connected)
    case _ =>
      stash()
  }
}

object BecomeHotswap extends App{
  import UserStorage._

  val system = ActorSystem("hotswap-become")
  val userStorage = system.actorOf(Props[UserStorage], "user-storage")

  userStorage ! Operation(DBOperation.Create, Some(User("admin", "admin@mail.com")))
  userStorage ! Connect
  userStorage ! Disconnect

  Thread.sleep(100)

  system.terminate()
}