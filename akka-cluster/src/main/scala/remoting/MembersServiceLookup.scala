package remoting

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import remoting.Worker.Work

/**
  * Created by sankova on 11/7/16.
  */
object MemberServiceLookup extends App {
  val config = ConfigFactory.load.getConfig("MemberServiceLookup")
  val system = ActorSystem("MemberServiceLookup", config)
  val worker = system.actorSelection("akka.tcp://MembersService@127.0.0.1:2552/user/remote-worker")
  worker ! Worker.Work("Hi Remote Actor")
}