package api

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MapAsyncDemo extends App {

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


  Source(1 to 100)
    .mapAsync(100)(t => {
      if (t == 30) {
        Thread.sleep(10000)
      }
      Future {t.toString}
    })
    .to(Sink.foreach(println)).run()

}
