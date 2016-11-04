package tweets.reactive

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import twitter4j.auth.AccessToken
import twitter4j._
import twitter4j.conf.ConfigurationBuilder

import scala.collection.JavaConverters._

/**
  * Created by sankova on 11/3/16.
  */
object TwitterConfiguration {
  val config = ConfigFactory.load.getConfig("Twitter")

  val apiKey = config.getString("apiKey")
  val apiSecret = config.getString("apiSecret")
  val accessToken = config.getString("accessToken")
  val accessTokenSecret = config.getString("accessTokenSecret")
}

object TwitterClient {
  def getInstance: Twitter = {
    val configBuilder = new ConfigurationBuilder()
    configBuilder.setDebugEnabled(true)
      .setDebugEnabled(true)
      .setOAuthConsumerKey(TwitterConfiguration.apiKey)
      .setOAuthConsumerSecret(TwitterConfiguration.apiSecret)
      .setOAuthAccessToken(TwitterConfiguration.accessToken)
      .setOAuthAccessTokenSecret(TwitterConfiguration.accessTokenSecret)

    val twitterFactory = new TwitterFactory(configBuilder.build())
    twitterFactory.getInstance()
  }

  def retrieveTweets(term: String): Iterator[Status] = {
    val query = new Query(term)
    query.setCount(100)
    getInstance.search(query).getTweets.asScala.iterator
  }
}

class TwitterStreamClient(val actorSystem: ActorSystem) {
  val factory = new TwitterStreamFactory(new ConfigurationBuilder().build())
  val twitterStream = factory.getInstance()

  def init = {
    twitterStream.setOAuthConsumer(TwitterConfiguration.apiKey, TwitterConfiguration.apiSecret)
    twitterStream.setOAuthAccessToken(new AccessToken(TwitterConfiguration.accessToken, TwitterConfiguration.accessTokenSecret))
    twitterStream.addListener(statusListener)
    twitterStream.sample()
  }

  def statusListener = new StatusListener() {
    def onStatus(status: Status) {
      actorSystem.eventStream.publish(Tweet(Author(status.getUser.getScreenName), status.getText))
    }

    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}

    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}

    def onException(ex: Exception) {
      ex.printStackTrace
    }

    def onScrubGeo(arg0: Long, arg1: Long) {}

    def onStallWarning(warning: StallWarning) {}
  }

  def stop = {
    twitterStream.cleanUp
    twitterStream.shutdown
  }
}
