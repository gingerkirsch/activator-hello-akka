import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike}
import org.scalatest.matchers.MustMatchers
import akka.stream
import akka.stream.testkit.scaladsl.{TestSink, TestSource}

/**
  * Created by sankova on 11/3/16.
  */
class StreamKitSpec extends TestKit(ActorSystem("test-system")) with ImplicitSender with FlatSpecLike
  with BeforeAndAfterAll with MustMatchers {
  import system.dispatcher

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  implicit val flowMaterializer = ActorMaterializer()

  "With Stream TestKit" should "use a TestSink to test a source" in {
    val sourceUnderTest = Source(1 to 4).filter(_ < 3).map(_ * 2)
    sourceUnderTest.runWith(TestSink.probe[Int])
      .request(2)
      .expectNext(2,4)
      .expectComplete()
  }

  it should "use a TestSource to test a sink" in{
    val sinkUnderTest = Sink.cancelled

    TestSource.probe[Int]
      .toMat(sinkUnderTest)(Keep.left)
      .run()
      .expectCancellation()
  }

}
