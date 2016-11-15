package remoting

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import remoting.Worker.Work

/**
  * Created by sankova on 11/7/16.
  */
object MembersService extends App {
  val config = ConfigFactory.load.getConfig("MembersService")
  val system = ActorSystem("MembersService", config)
  val worker = system.actorOf(Props[Worker], "remote-worker")
  println(s"Worker actor path is ${worker.path}")
}

