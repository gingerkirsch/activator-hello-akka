package io

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Framing, Sink}
import akka.util.ByteString

/**
  * Created by sankova on 11/3/16.
  */
object ReadStreamFromFile extends App {
  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val flowMaterializer = ActorMaterializer()

  val source = FileIO.fromPath(Paths.get("akka-streams/src/main/resources/log"))

  // parse chunks of bytes into lines
  val flow = Framing.delimiter(ByteString(System.lineSeparator()),
    maximumFrameLength = 512,
    allowTruncation = true).map(_.utf8String)

  val sink = Sink.foreach(println)

  source.via(flow).runWith(sink).andThen {
    case _ =>
      system.terminate()
  }

}
