package consumer.stream

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.{KStream, KStreamBuilder, KTable, ValueMapper}
import java.util
import java.util.Properties
import scala.collection.JavaConverters._


object WordCountApplication {
  @throws[Exception]
  def main(args: Array[String]): Unit = {
//    val config = new Properties
//    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application")
//    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-broker1:9092")
//    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass)
//    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass)
//    val builder = new KStreamBuilder
//    val textLines: KStream[String, String] = builder.stream("TextLinesTopic")
//    val wordCounts = textLines
//      .flatMapValues(new ValueMapper[String, Iterable[String]] {
//        override def apply(value: String) = value.toLowerCase.split("\\W+").toList
//      })
//    //      .groupBy((key: String, word: String) => word)
////      .count("Counts")
//    val str = Serdes.String()
//    val long = Serdes.Long()
//    wordCounts.to(str, long, "WordsWithCountsTopic")
//    val streams = new KafkaStreams(builder, config)
//    streams.start()
//    Runtime.getRuntime.addShutdownHook(new Thread(streams.close))
  }
}