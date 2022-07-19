package connectors

import cats.data.EitherT
import models.{APIError, BookModel, DataModel}
import org.mongodb.scala.result
import play.api.libs.json.{JsError, JsSuccess, OFormat}
import play.api.libs.ws.{WSClient, WSResponse}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

  class LibraryConnector @Inject()(ws: WSClient) { //ws allows you to talk to api
    def get[Response](url: String)(implicit rds: OFormat[Response], ec: ExecutionContext): EitherT[Future, APIError, DataModel] = {
      val request = ws.url(url)
      val response = request.get()
      EitherT {
        response
          .map {
            result =>
            result.json.validate[BookModel] match {
              case JsSuccess(googleBookValue, _) =>
                Right(DataModel(googleBookValue.items.head.id, googleBookValue.items.head.volumeInfo.title, googleBookValue.items.head.volumeInfo.description, googleBookValue.items.head.volumeInfo.pageCount))
              case JsError(errors) => Left(APIError.BadAPIResponse(500, "Could not find book"))

      }
    }
  }}}
//mapping onto model - taking different fields
//make new data model in return
//new instance of data model case class with values assigned from google-books json