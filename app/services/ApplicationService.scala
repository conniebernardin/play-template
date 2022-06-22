package services

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import models.{APIError, DataModel}
import play.api.libs.json.{JsError, JsSuccess, JsValue}
import play.api.mvc.Request
import play.api.mvc.Results.Status
import repositories.DataRepository
import views.html.helper.input

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApplicationService @Inject() (val dataRepository: DataRepository)(implicit val ec: ExecutionContext) {

  def read(id: String): Future[Either[APIError, DataModel]] =
    dataRepository.read(id).map{
      case Right(book: DataModel) => Right(book)
      case Left(error) => Left(APIError.BadAPIResponse(404, "Could not find book"))
    }

  def create(input: Request[JsValue]): Future[Either[APIError, DataModel]] =
    input.body.validate[DataModel] match {
      case JsSuccess(value, _) => dataRepository.create(value)
      case JsError(errors) => Future(Left(APIError.BadAPIResponse(404, "Could not create book")))
    }

  def delete(id: String): Future[Either[APIError, String]] =
    dataRepository.delete(id).map{
      case Right(message) => Right("Book successfully deleted")
      case Left(error) => Left(APIError.BadAPIResponse(404, "Could not delete book"))
    }

  def update(id: String, input: Request[JsValue]): Future[Either[APIError, DataModel]] =
    input.body.validate[DataModel] match {
      case JsSuccess(book, _) => dataRepository.update(id)
      case JsError(error) => Future(Left(APIError.BadAPIResponse(404, "Could not update book")))
    }
}
