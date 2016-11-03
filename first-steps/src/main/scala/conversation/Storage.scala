package conversation

import akka.actor.{Actor, Props}
import conversation.Storage.AddUser

/**
  * Created by sankova on 11/2/16.
  */
object Storage{
  sealed trait StorageMsg
  case class AddUser(user:User) extends StorageMsg
  val props = Props[Storage]
}
class Storage extends Actor{
  var users = List.empty[User]

  def receive = {
    case AddUser(user) => {
      println(s"Storage: $user has been added")
      users = user :: users
    }

  }

}
