package models

import play.api.libs.json.{Format, Json}

case class BookModel(items: List[Item])
object BookModel {
  implicit val formats: Format[BookModel] = Json.format[BookModel]
}

case class volumeInfo(title: String, description: String, pageCount: Int)
object volumeInfo{
  implicit val formats: Format[volumeInfo] = Json.format[volumeInfo]
}

case class Item(id: String, volumeInfo:volumeInfo){
}
object Item{
  implicit val formats: Format[Item] = Json.format[Item]
}
