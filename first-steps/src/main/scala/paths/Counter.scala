package paths

import akka.actor.Actor
import paths.Counter.{Dec, Inc}

/**
  * Created by sankova on 11/5/16.
  */
class Counter extends Actor {
  var count = 0
  def receive = {
    case Inc(x) =>
      count += x
    case Dec(x) =>
      count -= x
  }
}

object Counter {
  final case class Inc(num: Int)
  final case class Dec(num: Int)
}
