package producer

import akka.stream.scaladsl.Source

object PlainSinkDemo extends App {

  import akka.actor.ActorSystem
  import akka.kafka.ProducerSettings
  import akka.kafka.scaladsl.Producer
  import akka.stream.ActorMaterializer
  import org.apache.kafka.clients.producer.ProducerRecord
  import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

  implicit val system = ActorSystem("CommitConsumerToFlowProducerMain")
  implicit val materializer = ActorMaterializer()

  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")

  val done = Source(1 to 10)
    .map(_.toString).map(t => {println(t); t})
    .map { elem =>
      new ProducerRecord[Array[Byte], String]("topic1", elem)
    }
    .runWith(Producer.plainSink(producerSettings))

}
