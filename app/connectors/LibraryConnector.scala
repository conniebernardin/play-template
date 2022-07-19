package connectors

import cats.data.EitherT
import models.{APIError, BookModel, DataModel}
import org.mongodb.scala.result
import play.api.libs.json.{JsError, JsSuccess, OFormat}
import play.api.libs.ws.{WSClient, WSResponse}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

  class LibraryConnector @Inject()(ws: WSClient) { //ws allows you to talk to api
    def get[Response](url: String)(implicit rds: OFormat[Response], ec: ExecutionContext): EitherT[Future, APIError, Response] = {
      val request = ws.url(url)
      val response = request.get()
      EitherT {
        response
          .map {
            result =>
            result.json.validate[BookModel] match {
              case JsSuccess(value, _) =>
                Right(DataModel(value.items.head._id, value.items.head.bookInfo.title, value.items.head.bookInfo.description, value.items.head.bookInfo.pageCount))
              case JsError(errors) => Left(APIError.BadAPIResponse(500, "Could not find book"))

      }
    }
  }}}
//mapping onto model - taking different fields
//make new data model in return
//new instance of data model case class with values assigned from googlebooks json