package services

import models.{APIError, DataModel}
import repositories.DataRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApplicationService @Inject() (val dataRepository: DataRepository,
                                    implicit val ec: ExecutionContext) {

  def read(id: String): Future[Either[APIError, DataModel]] =
    dataRepository.read(id).map{
      case Right(book: DataModel) => Right(book)
      case Left(errors) => Left(APIError.BadAPIResponse(404, "Could not find book"))
    }

}
