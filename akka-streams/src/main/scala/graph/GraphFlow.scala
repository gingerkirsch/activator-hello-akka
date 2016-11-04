package graph

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.ClosedShape
import akka.stream.scaladsl._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

/**
  * Created by sankova on 11/3/16.
  */
object GraphFlow extends App {
  implicit val system = ActorSystem("graph-flow")
  implicit val flowMaterializer = ActorMaterializer()
  import system.dispatcher
  val in = Source(1 to 10)
  val out = Sink.foreach[Int](println)
  val f1, f3 = Flow[Int].map(_ + 10)
  val f2 = Flow[Int].map(_ * 5)
  val f4 = Flow[Int].map(_ + 0)
  val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder =>

    import GraphDSL.Implicits._
    val broadCast = builder.add(Broadcast[Int](2))
    val merge = builder.add(Merge[Int](2))
    in ~> f1 ~> broadCast ~> f2 ~> merge ~> f3 ~> out
                broadCast ~> f4 ~> merge
    ClosedShape
  })

  val materialized = RunnableGraph.fromGraph(graph).run()

  system.terminate()
}
