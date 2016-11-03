package creation

import akka.actor.ActorSystem
import creation.MusicPlayer.StartMusic

/**
  * Created by sankova on 11/2/16.
  */
object Creator extends App{
  val system = ActorSystem("creation")
  val player = system.actorOf(MusicPlayer.props, "player")
  player ! StartMusic
}
