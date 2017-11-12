package errorHandling

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

object StopStreamDemo extends App {

  val decider: Supervision.Decider = {
    case _: IllegalArgumentException => Supervision.resume //restart //stop by default
    case _ => Supervision.Stop
  }

  implicit val system = ActorSystem("sourceDemo")
  implicit val materializer = ActorMaterializer(ActorMaterializerSettings(system)
    .withSupervisionStrategy(decider))

  Source(1 to 100)
    .mapAsync(100)(t => {
      if (t == 30) {
        throw new IllegalArgumentException("some error")
      }
      Future {t.toString}
    })
    .to(Sink.foreach(println)).run()

}
