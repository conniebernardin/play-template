package repositories

import com.mongodb.client.result.DeleteResult
import models.{APIError, DataModel}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.{empty, equal}
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.model._
import play.api.libs.json.JsValue
import play.libs.Json
import org.mongodb.scala.result
import org.mongodb.scala.result.InsertOneResult
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util

@Singleton
class DataRepository @Inject()(
                                mongoComponent: MongoComponent
                              )(implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
  collectionName = "dataModels",
  mongoComponent = mongoComponent,
  domainFormat = DataModel.formats,
  indexes = Seq(IndexModel(
    Indexes.ascending("_id")
  )),
  replaceIndexes = false
) {

  def create(): Future[Either[APIError, DataModel]] =
    collection
      .insertOne(book)
      .toFutureOption()
      .map{
          case Some(value) if value.wasAcknowledged() => Right(book)
          case _ => Left(APIError.BadAPIResponse(400, "book could not be created"))
      }

  private def byID(id: String): Bson =
    Filters.and(
      Filters.equal("_id", id)
    )

  def read(id: String): Future[Either[APIError, DataModel]] = {
    collection.find(byID(id)).headOption flatMap {
      case Some(data) =>
        Future(Right(data))
     case _ =>
       Future(Left(APIError.BadAPIResponse(400, "Could not read book")))
    }
  }

  def update(id: String, book: DataModel): Future[Either[APIError, DataModel]] =
    collection.replaceOne(
      filter = byID(id),
      replacement = book,
      options = new ReplaceOptions().upsert(true) //What happens when we set this to false?
    ).toFutureOption().map {
      case Some(value) if value.wasAcknowledged() => Right(book)
      case _ => Left(APIError.BadAPIResponse(400, "book could not be updated"))
    }

  def delete(id: String): Future[Either[APIError, String]] =
    collection.deleteOne(
      filter = byID(id)
    ).toFutureOption().map{
      case Some(value) if value.getDeletedCount == 1 => Right("Book successfully deleted")
      case _ => Left(APIError.BadAPIResponse(400, "Could not delete book"))
    }

  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ()) //Hint: needed for tests




  val book: DataModel = DataModel("id1", "Frankenstein", "Mary Shelley", 200_000)
}
