package behavior

import akka.actor.{Actor, ActorSystem, FSM, Props, Stash}

/**
  * Created by sankova on 11/6/16.
  */
object UserStorageFSM {
  //FSM State
  sealed trait State
  case object Connected extends State
  case object Disconnected extends State

  //FSM Data
  sealed trait Data
  case object EmptyData extends Data

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

class UserStorageFSM extends FSM[UserStorageFSM.State, UserStorageFSM.Data] with Stash {
  import UserStorageFSM._

  //1.define start with
  startWith(Disconnected, EmptyData)

  //2.define states
  when(Disconnected){
    case Event(Connect, _) =>
      println(s"UserStorage connected to DB")
      unstashAll()
      goto(Connected) using (EmptyData)
    case Event(_,_) =>
      stash()
      stay using EmptyData
  }

  when(Connected){
    case Event(Disconnect, _) =>
      println(s"UserStorage disconnected to DB")
      goto(Disconnected) using (EmptyData)
    case Event(Operation(op, user),_) =>
      println(s"UserStorage receive $op operation to do in user: $user")
      stay using EmptyData
  }

  //3.initialize
  initialize()
}

object FSMApp extends App{
  import UserStorage._

  val system = ActorSystem("hotswap-fsm")
  val userStorage = system.actorOf(Props[UserStorage], "user-storage")

  userStorage ! Operation(DBOperation.Create, Some(User("admin", "admin@mail.com")))
  userStorage ! Connect
  userStorage ! Disconnect

  Thread.sleep(100)

  system.terminate()
}