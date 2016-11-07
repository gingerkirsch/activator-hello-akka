package fsm.cqrs

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.stream.ActorMaterializer
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.stream.scaladsl.Source

/**
  * Created by sankova on 11/7/16.
  */
object Reporter extends App {
  val system = ActorSystem("persistent-query")
  implicit val mat = ActorMaterializer()(system)

  val queries = PersistenceQuery(system).readJournalFor[LeveldbReadJournal](
    LeveldbReadJournal.Identifier
  )

  val evts: Source[EventEnvelope, NotUsed] =
    queries.eventsByPersistenceId("account")

  evts.runForeach { evt => println(s"Event $evt")}

  Thread.sleep(1000)

  system.terminate()
}
