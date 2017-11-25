package api

import java.util.concurrent.Executors

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Attributes, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

object GroupWithinDemo extends App {
  implicit val system = ActorSystem("sourceDemo")

  implicit val materializer = ActorMaterializer()

  //  Source(1 to 100).groupedWithin(50, 3 seconds).to(Sink.foreach(println)).run()
//    Source.fromIterator(() => new Iterator[Int] {
//      var count = 0
//      override def hasNext = true
//      override def next() = {
//        count = count + 1
//        Thread.sleep(1000)
//        count
//      }})
//      .withAttributes(Attributes.asyncBoundary)
//      .groupedWithin(30, 5 seconds)
//      .to(Sink.foreach(println))
//      .run()


//  val iterator = new Iterator[Int] {
//    var count = 0
//
//    override def hasNext = true
//
//    override def next() = {
//      count = count + 1
//      Thread.sleep(1000)
//      count
//    }
//  }

  implicit val t = ExecutionContext.fromExecutor(
    Executors.newFixedThreadPool(1))

  Source.fromIterator(() => new Iterator[Future[Int]] {
    var count = 0
    override def hasNext = true
    override def next() = {
      println(count)
      Future {
        count = count + 1
        count
      }
    }
  }).via(Flow[Future[Int]].mapAsync(1)(identity))
    .groupedWithin(30, 5 seconds).map(t => {
    Thread.sleep(1000)
    t
  }).to(Sink.foreach(t => println(t)))
    .run()

//  Source.unfoldAsync(0)( t => {
//    if (iterator.hasNext) {
//      Future(Some((iterator.next(), t)))
//    } else {
//      Future.successful(None)
//    }
//
//  }).groupedWithin(30, 5 seconds).to(Sink.foreach(println)).run()
//
//
//  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
//    import GraphDSL.Implicits._
//    val in = Source(1 to 10)
//    val out = Sink.ignore
//
//    val bcast = builder.add(Broadcast[Int](2))
//    val merge = builder.add(Merge[Int](2))
//
//    val f1, f2, f3, f4 = Flow[Int].map(_ + 10)
//
//    in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> out
//    bcast ~> f4 ~> merge
//    ClosedShape
//  })
}
