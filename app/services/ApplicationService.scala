package services

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import models.{APIError, DataModel}
import play.api.mvc.Results.Status
import repositories.DataRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApplicationService @Inject() (val dataRepository: DataRepository)(implicit val ec: ExecutionContext) {

  def read(id: String): Future[Either[APIError, DataModel]] =
    dataRepository.read(id).map{
      case Right(book: DataModel) => Right(book)
      case Left(error) => Left(APIError.BadAPIResponse(404, "Could not find book"))
    }

  def create(): Future[Either[APIError, DataModel]] =
    dataRepository.create().map{
        case Right(book: DataModel) => Right(book)
        case Left(error) => Left(APIError.BadAPIResponse(404, "Could not create book"))
    }

  def delete(id: String): Future[Either[APIError, String]] =
    dataRepository.delete(id).map{
      case Right(message) => Right("Book successfully deleted")
      case Left(error) => Left(APIError.BadAPIResponse(404, "Could not delete book"))
    }

  def update(id: String, book: DataModel): Future[Either[APIError, DataModel]] =
    dataRepository.update(id, book).map{
      case Right(book:DataModel) => Right(book)
      case Left(error) => Left(APIError.BadAPIResponse(404, "Could not update book"))
    }
}
