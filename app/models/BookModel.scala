package models

import play.api.libs.json.{Format, Json}

case class BookModel(items: List[Item])
object BookModel {
  implicit val formats: Format[BookModel] = Json.format[BookModel]
}

case class VolumeInfo(title: String, description: String, pageCount: Int)
object VolumeInfo{
  implicit val formats: Format[VolumeInfo] = Json.format[VolumeInfo]
}

case class Item(id: String, volumeInfo:VolumeInfo){
}
object Item{
  implicit val formats: Format[Item] = Json.format[Item]
}
