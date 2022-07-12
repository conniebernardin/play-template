package repositories

import com.google.inject.ImplementedBy
import com.mongodb.client.result.DeleteResult
import models.{APIError, DataModel}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.{empty, equal}
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.model._
import play.api.libs.json.JsValue
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util

@ImplementedBy(classOf[DataRepository])
trait MockRepositoryTrait {
  def create(book: DataModel): Future[Either[APIError, DataModel]]
  def read(id: String): Future[Either[APIError, DataModel]]
  def readByName(name: String): Future[Either[APIError, DataModel]]
  def update(id: String): Future[Either[APIError, DataModel]]
  def updateField(id: String, field: String, updatedValue: String): Future[Option[DataModel]]
  def delete(id: String): Future[Either[APIError, String]]
}


@Singleton
class DataRepository @Inject() (
                                mongoComponent: MongoComponent
                              )(implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
  collectionName = "dataModels",
  mongoComponent = mongoComponent,
  domainFormat = DataModel.formats,
  indexes = Seq(IndexModel(
    Indexes.ascending("_id")
  )),
  replaceIndexes = false
) with MockRepositoryTrait {

  def create(book: DataModel): Future[Either[APIError, DataModel]] =
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

  private def byName(name: String): Bson =
    Filters.and(
      Filters.equal("name", name)
    )

  def readByName(name: String): Future[Either[APIError, DataModel]] = {
    collection.find(byName(name)).headOption() flatMap{
      case Some(data) =>
        Future(Right(data))
      case _ =>
        Future(Left(APIError.BadAPIResponse(400, "could not find book name")))
    }
  }

  def read(id: String): Future[Either[APIError, DataModel]] = {
    collection.find(byID(id)).headOption flatMap {
      case Some(data) =>
        Future(Right(data))
     case _ =>
       Future(Left(APIError.BadAPIResponse(400, "Could not read book")))
    }
  }

  def update(id: String): Future[Either[APIError, DataModel]] =
    collection.replaceOne(
      filter = byID(id),
      replacement = book,
      options = new ReplaceOptions().upsert(true)
    ).toFutureOption().map {
      case Some(value) if value.wasAcknowledged() => Right(book)
      case _ => Left(APIError.BadAPIResponse(400, "book could not be updated"))
    }

  def updateField(id: String, field: String, updatedValue: String): Future[Option[DataModel]] = {
    collection.findOneAndUpdate(
      equal("_id", id),
      set(field, updatedValue),
      options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
    ).toFutureOption()
  }

  def delete(id: String): Future[Either[APIError, String]] =
    collection.deleteOne(
      filter = byID(id)
    ).toFutureOption().map{
      case Some(value) if value.getDeletedCount == 1 => Right("Book successfully deleted")
      case _ => Left(APIError.BadAPIResponse(400, "Could not delete book"))
    }

  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ())


  val book: DataModel = DataModel("abcd", "Frankenstein", "Mary Shelley", 200_000)
}

