package models

import play.api.libs.json.{Format, Json}

case class BookModel(items: List[Item])
object BookModel {
  implicit val formats: Format[BookModel] = Json.format[BookModel]
}

case class bookInfo(title: String, description: String)
object bookInfo{
  implicit val formats: Format[bookInfo] = Json.format[bookInfo]
}

case class Item(_id: String, bookInfo:bookInfo){
}
object Item{
  implicit val formats: Format[Item] = Json.format[Item]
}
