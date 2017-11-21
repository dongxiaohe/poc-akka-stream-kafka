package zendesk.data.demo

import java.util
import java.util.Properties

import scala.collection.JavaConverters._
import akka.kafka.scaladsl.Consumer
import org.apache.kafka.clients.consumer.{KafkaConsumer, OffsetAndMetadata}
import org.apache.kafka.common.TopicPartition

import scala.util.{Failure, Try}

object PollExample extends App {

  import org.apache.kafka.clients.consumer.KafkaConsumer
  import org.apache.kafka.common.serialization.StringDeserializer

  val props = new Properties
  props.put("bootstrap.servers", "localhost:9092")
  props.put("group.id", "consumer-tutorial")
  props.put("enable.auto.commit", "false")
  props.put("max.poll.records", "10")
//  props.put("auto.offset.reset", "none")
  props.put("key.deserializer", classOf[StringDeserializer].getName)
  props.put("value.deserializer", classOf[StringDeserializer].getName)
  val consumer = new KafkaConsumer[String, String](props)

  consumer.subscribe(List("bar").asJava)
  consumer.poll(1000)
//  consumer.assign(List(new TopicPartition("foo", 0)).asJava)
//  consumer.seek(new TopicPartition("bar", 0), 1)
//  consumer.commitSync(Map(new TopicPartition("bar", 0) -> new OffsetAndMetadata(1)).asJava)
//  consumer.seek(new TopicPartition("maxwell-test", 1), 1)

//  consumer.poll(1000)
//  consumer.seekToBeginning(List(new TopicPartition("maxwell-test", 0)).asJava)

  var count = 0
  while(true) {
    Try {
      println("start tracking ......................... ", count)
      val records = consumer.poll(1000)
//      consumer.seekToBeginning(List(new TopicPartition("maxwell-test", 0)).asJava)
      count = count + 1
      records.forEach(t => println(t.value()))
      consumer.commitSync(Map(new TopicPartition("bar", 0) -> new OffsetAndMetadata(count)).asJava)
//      Thread.sleep(3000)

    } match {
      case Failure(ex) => sys.exit(0)
      case _ =>
    }
  }

//  val records = consumer.poll(1000)
//  records.forEach(t => println(t.value()))
//
  consumer.close()
}
