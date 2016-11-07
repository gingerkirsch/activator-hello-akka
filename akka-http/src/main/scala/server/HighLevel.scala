package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Path
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

/**
  * Created by sankova on 11/7/16.
  */
object HighLevel extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher
  val route =
    path(""){
      get {
        complete("Hello Akka HTTP Server Side API - High level :)")
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
}
