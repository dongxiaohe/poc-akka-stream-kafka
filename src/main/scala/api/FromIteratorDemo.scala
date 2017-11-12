package api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.javadsl.Sink
import akka.stream.scaladsl.Source

object FromIteratorDemo extends App {
  implicit val system = ActorSystem("sourceDemo")
  implicit val materializer = ActorMaterializer()

  Source.fromIterator[Int](() => new Iterator[Int] {
    var count = 0

    override def hasNext = false

    override def next() = {
      count = count + 1
      count
    }
  }).to(Sink.foreach(t => println(t)))
    .run()

}
