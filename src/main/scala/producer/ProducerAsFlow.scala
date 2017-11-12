package producer

import akka.kafka.{ConsumerSettings, ProducerMessage, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.{Sink, Source}
import consumer.ConsumeAndProduce.system
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

object ProducerAsFlow extends App {

  import akka.actor.ActorSystem
  import akka.kafka.ProducerSettings
  import akka.kafka.scaladsl.Producer
  import akka.stream.ActorMaterializer
  import org.apache.kafka.clients.producer.ProducerRecord
  import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

  implicit val system = ActorSystem("forward-to-different-topic")
  implicit val materializer = ActorMaterializer()

  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")

  val consumerSettings =
    ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers("localhost:9092")
      .withGroupId("CommittableSourceConsumer")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  Consumer.committableSource(consumerSettings, Subscriptions.topics("topic1"))
    .map { n =>
      // val partition = math.abs(n) % 2
      val partition = 0
      ProducerMessage.Message(new ProducerRecord[Array[Byte], String](
        "topic2", partition, null, n.toString
      ), n)
    }
    .via(Producer.flow(producerSettings))
        .map { n =>
          val partition = 0
          ProducerMessage.Message(new ProducerRecord[Array[Byte], String](
            "topic3", partition, null, n.toString
          ), n)
        }
    .via(Producer.flow(producerSettings))
    .runWith(Sink.ignore)
}
