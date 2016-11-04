package io

import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape, IOResult}
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by sankova on 11/3/16.
  */
object WriteStreamFromFile extends App{
  implicit val system = ActorSystem("io")
  import system.dispatcher
  implicit val flowMaterializer = ActorMaterializer()

  // Source
  val source = Source(1 to 10000).filter(isPrime)

  // Sink
  val sink = FileIO.toPath(Paths.get("akka-streams/target/primes.txt"))

  // file output sink
  val fileSink = Flow[Int]
    .map(i => ByteString(i.toString))
    .toMat(sink)((_, bytesWritten) => bytesWritten)

  // console output sink
  val consoleSink = Sink.foreach[Int](println)

  // send primes to both file sink and console sink using graph API
  val graph = GraphDSL.create(fileSink, consoleSink)((file, _) => file) { implicit builder =>
    (file, console) =>
      import GraphDSL.Implicits._
      val broadcast = builder.add(Broadcast[Int](2))

      source ~> broadcast ~> file
                broadcast ~> console

      ClosedShape
  }

  val materialized = RunnableGraph.fromGraph(graph).run()


  materialized.onComplete {
    case Success(_) =>
      system.terminate()
    case Failure(e) =>
      println(s"Failure: ${e.getMessage}")
      system.terminate()
  }

  def isPrime(n: Int): Boolean = {
    if (n <= 1) false
    else if (n == 2) true
    else !(2 to (n - 1)).exists(x => n % x == 0)
  }
}
