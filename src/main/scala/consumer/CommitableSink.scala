package consumer

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{ConsumerSettings, ProducerMessage, ProducerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, ByteArraySerializer, StringDeserializer, StringSerializer}
import producer.PlainSinkDemo.system

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object CommitableSink extends App {

  implicit val system = ActorSystem("CommitConsumerToFlowProducerMain-123")
  implicit val materializer = ActorMaterializer()

  val consumerSettings =
    ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers("localhost:9092")
      .withGroupId("CommittableSourceConsumer")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "false")

  val done =
    Consumer.committableSource(consumerSettings, Subscriptions.topics("topic1"))
      .mapAsync(1) { msg =>
//        if (msg.record.value().equals("7")) {
//          throw new Exception("123123123")
//        }
        println("msg offset: ", msg.committableOffset.partitionOffset.offset)
        println("msg topic: ", msg.record.topic())
        println("msg value: ", msg.record.value())
        msg.committableOffset.commitScaladsl()
      }
      .runWith(Sink.ignore)

}
