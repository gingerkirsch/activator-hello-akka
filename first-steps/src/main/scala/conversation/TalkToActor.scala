package conversation

import akka.actor.ActorSystem

/**
  * Created by sankova on 11/2/16.
  */
object TalkToActor extends App {
  val system = ActorSystem("talk-to-actor")
  val checker = system.actorOf(Checker.props, "checker")
  val storage = system.actorOf(Storage.props, "storage")
  val recorder = system.actorOf(Recorder.props(checker, storage), "conversation")

  recorder ! Recorder.NewUser(User("John", "john@mail.com"))
  Thread.sleep(100)
  system.terminate()
}
