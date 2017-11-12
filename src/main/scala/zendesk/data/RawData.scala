package zendesk.data

final case class RawData(timestamp: Long, transactionName: String, transactionValue: String)

case class Transaction(name: String, rawDataList: Vector[RawData], done: Boolean)