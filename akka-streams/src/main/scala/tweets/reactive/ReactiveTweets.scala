package tweets.reactive

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.collection.immutable
import twitter4j.Status

/**
  * Created by sankova on 11/3/16.
  */
object ReactiveTweets extends App {
/*  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher
  val myHashtag = Hashtag("#lxwebsummit2016")
  val tweets: Source[Tweet, Unit]
  val authors: Source[Author, NotUsed] =
    tweets
      .filter(_.hashtags.contains(myHashtag)
      .map(_.author)
  //val source = Source((() => TwitterClient.retrieveTweets("#lxwebsummit2016")).apply().)

  /*val normalize = Flow[Status].map{ t =>
    Tweet(Author(t.getUser().getName()), t.getText())
  }*/

  authors.runWith(Sink.foreach(println))*/
  import system.dispatcher
  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()
  val myHashtag = Hashtag("#lxwebsummit2016")
  val tweets: Source[Status, NotUsed] = Source.fromIterator(() => TwitterClient.retrieveTweets(myHashtag.name))
  val normalize = Flow[Status].map{ t =>
    Tweet(Author(t.getUser().getName()), t.getText())
  }
  val authors =Sink.foreach(println)

  tweets.via(normalize).runWith(authors)
}
