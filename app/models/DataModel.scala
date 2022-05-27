package models

import play.api.libs.json.{Json, OFormat}

case class DataModel(_id: String,
                     name: String,
                     description: String,
                     numSales: Int){

  val bookOne: DataModel = DataModel("id1", "Frankenstein", "Mary Shelley", 2000)
}

object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
}