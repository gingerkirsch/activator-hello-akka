package creation

import akka.actor.{Actor, Props}
import creation.MusicController.Play
import creation.MusicPlayer.{StartMusic, StopMusic}

/**
  * Created by sankova on 11/2/16.
  */
object MusicPlayer {
  sealed trait PlayMsg
  case object StopMusic extends PlayMsg
  case object StartMusic extends PlayMsg
  val props = Props[MusicPlayer]
}

class MusicPlayer extends Actor{
  def receive = {
    case StopMusic => println("please don't stop the music")
    case StartMusic => {
      val controller = context.actorOf(MusicController.props, "controller")
      controller ! Play}
    case _ => println("unknown message")
  }
}
