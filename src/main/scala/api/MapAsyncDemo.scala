package api

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Attributes, Supervision}
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object MapAsyncDemo extends App with ExecutionDuration {

  implicit val system = ActorSystem("sourceDemo")
  implicit val materializer = ActorMaterializer()

  //  Source(1 to 100)
  //    .mapAsync(100)(t => {
  //      Future {t.toString}
  //    })
  //    .to(Sink.foreach(println)).run()

  //  Source(1 to 100)
  //    .mapAsyncUnordered(100)(t => {
  //      Future {t.toString}
  //    })
  //    .to(Sink.foreach(println)).run()


  //  Source(1 to 100)
  //    .mapAsync(100)(t => {
  //      if (t == 30) {
  //        Thread.sleep(10000)
  //      }
  //      Future {t.toString}
  //    })
  //    .to(Sink.foreach(println)).run()


  //  Source(1 to 100)
  //    .mapAsync(100)(t => {
  //      Thread.sleep(1000)
  //      Future {t.toString}
  //    })
  //      .groupedWithin(10, 500 milliseconds)
  //    .to(Sink.foreach(println)).run()

  //  Source(1 to 100)
  //    .mapAsync(100)(t => {
  //      Future {Thread.sleep(1000); t.toString}
  //    })
  //    .groupedWithin(10, 500 milliseconds)
  //    .to(Sink.foreach(println)).run()

  //  Source.repeat(1)
  //    .mapAsync(100)(t => {
  //      Thread.sleep(500)
  //      Future { t.toString}
  //    })
  //    .groupedWithin(10, 500 milliseconds)
  //    .to(Sink.foreach(println)).run()

  //20180ms
  //11186ms

  val t0 = System.currentTimeMillis()
  Source(1 to 10)
    .map(t => {
      Thread.sleep(1000)
      t + 1
    })
    .withAttributes(Attributes.asyncBoundary)
    .map(t => {
      Thread.sleep(1000)
      t * 2
    })
    .runForeach(t => {
      println(t)
      if (t == 22 ) {
        val t1 = System.currentTimeMillis()
        println("Elapsed time: " + (t1 - t0) + "ms")
      }

    })

}
