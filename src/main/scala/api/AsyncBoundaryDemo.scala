package api

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Attributes}
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object AsyncBoundaryDemo extends App {

  implicit val system = ActorSystem("sourceDemo")
  implicit val materializer = ActorMaterializer()


  Source(1 to 100).mapAsync(100)(t => Future {t + 1}).mapAsync(100)(t => Future {t * 2}).map(println).to(Sink.ignore).run()
  Source(1 to 100).map(_ + 1).withAttributes(Attributes.asyncBoundary).map(_ * 2).map(t => println("async boundary", t)).to(Sink.ignore).run()

}
