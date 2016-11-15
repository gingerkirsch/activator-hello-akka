package remoting

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * Created by sankova on 11/7/16.
  */
object MembersServiceRemoteCreation extends App {
  val config = ConfigFactory.load.getConfig("MembersServiceRemoteCreation")
  val system = ActorSystem("MembersServiceRemoteCreation", config)
  val workerActor = system.actorOf(Props[Worker], "workerActorRemote")
  println(s"The remote path of worker Actor is ${workerActor.path}")
  workerActor ! Worker.Work("Hi Remote Worker")
}