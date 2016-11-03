package creation

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, Props}
import creation.MusicController.Play

/**
  * Example from Akka course of Salma Khater
  */
object MusicController {
  sealed trait ControllerMsg
  case object Play extends ControllerMsg
  case object Stop extends ControllerMsg
  def props = Props[MusicController]
}

class MusicController extends Actor{
  def receive = {
    case Play => println("playin...")
    case Stop => println("stopped...")
    case _ => println("unknown message")
  }
}