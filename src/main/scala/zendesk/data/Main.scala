package zendesk.data

import akka.NotUsed
import akka.stream.scaladsl.Flow

object Main extends App {

  import akka.actor.ActorSystem
  import akka.kafka.ProducerSettings
  import akka.kafka.scaladsl.Producer
  import akka.stream.ActorMaterializer
  import org.apache.kafka.clients.producer.ProducerRecord
  import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
  import akka.stream.scaladsl.Source

  implicit val system = ActorSystem("CommitConsumerToFlowProducerMain")
  implicit val materializer = ActorMaterializer()

  val rawStream = () => {
    List(
      RawData(1, "transaction 1", "starts"),
      RawData(1, "transaction 1", "1"),
      RawData(1, "transaction 1", "2"),
      RawData(1, "transaction 2", "starts"),
      RawData(1, "transaction 2", "1"),
      RawData(1, "transaction 2", "2"),
      RawData(1, "transaction 2", "3"),
      RawData(1, "transaction 1", "3"),
      RawData(1, "transaction 1", "ends"),
      RawData(1, "transaction 2", "4"),
      RawData(1, "transaction 3", "starts"),
      RawData(1, "transaction 3", "1"),
      RawData(1, "transaction 3", "2"),
      RawData(1, "transaction 3", "3"),
      RawData(1, "transaction 2", "ends"),
      RawData(1, "transaction 3", "4"),
      RawData(1, "transaction 3", "ends"),
      RawData(1, "transaction 4", "starts"),
      RawData(1, "transaction 4", "1"),
      RawData(1, "transaction 4", "2"),
      RawData(1, "transaction 4", "3"),
      RawData(1, "transaction 4", "4"),
      RawData(1, "transaction 4", "ends")
    ).toIterator
  }

  type TransactionList = Vector[Transaction]

  val appendRawDataToTransaction: (TransactionList, RawData) => TransactionList = (list, rawData) => {
    val index = list.indexOf(rawData)
    if (index != -1) {
      val transaction = list(index)
      rawData match {
        case RawData(_, _, "ends") =>
          list.updated(index, transaction.copy(done = true, rawDataList = transaction.rawDataList :+ rawData ))
        case t => list.updated(index, transaction.copy(rawDataList = transaction.rawDataList :+ rawData ))
      }
    } else {
      list :+ Transaction(rawData.transactionName, Vector(rawData), done = false)
    }
  }

  val flow: Flow[RawData, TransactionList, NotUsed] = Flow[RawData].scan(Vector[Transaction]())(appendRawDataToTransaction)

  Source.fromIterator(rawStream)

}
