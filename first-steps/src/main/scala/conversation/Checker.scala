package conversation

import akka.actor.{Actor, Props}
import conversation.Checker.{BlackUser, CheckUser, WhiteUser}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sankova on 11/2/16.
  */
object Checker{
  sealed trait CheckerMsg
  case class CheckUser(user: User) extends CheckerMsg

  sealed trait CheckerResponse
  case class WhiteUser(user: User) extends CheckerMsg
  case class BlackUser(user: User) extends CheckerMsg

  val props = Props[Checker]
}

class Checker extends Actor{
  val blacklist = List(User("Adam", "adam@mail.com"))

  def receive = {
    case CheckUser(user) if blacklist.contains(user) =>
      println(s"Checker: $user is in the blacklist")
      sender() ! BlackUser(user)
    case CheckUser(user) =>
      println(s"Checker: $user is not in the blacklist")
      sender() ! WhiteUser(user)
  }
}
