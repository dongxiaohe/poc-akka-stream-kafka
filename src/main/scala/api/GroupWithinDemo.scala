package api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.duration._

object GroupWithinDemo extends App {
  implicit val system = ActorSystem("sourceDemo")
  implicit val materializer = ActorMaterializer()

//  Source(1 to 100).groupedWithin(50, 3 seconds).to(Sink.foreach(println)).run()
  Source.fromIterator(() => new Iterator[Int] {
    var count = 0
    override def hasNext = true

    override def next() = {
      count = count + 1
      Thread.sleep(500)
      count
    }
  }).groupedWithin(1, 5 seconds).to(Sink.foreach(println)).run()
}
