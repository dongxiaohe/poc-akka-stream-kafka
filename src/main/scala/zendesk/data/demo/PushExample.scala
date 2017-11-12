package zendesk.data.demo

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.collection.JavaConverters._
import scala.util.{Failure, Try}

object PushExample extends App {

  import org.apache.kafka.clients.consumer.KafkaConsumer
  import org.apache.kafka.common.serialization.StringDeserializer

  val props = new Properties
  props.put("bootstrap.servers", "localhost:9092")
  props.put("acks", "all")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  val producer = new KafkaProducer[String, String](props)

  var count: Long = 1
  while(count <= 100000) {
    producer.send(new ProducerRecord[String, String]("bar", count.toString))
    count = count + 1
  }

  producer.close()

}
