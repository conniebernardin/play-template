package services

import akka.util.Helpers.Requiring
import baseSpec.BaseSpec
import cats.data.EitherT
import com.google.common.util.concurrent.Callables.returning
import connectors.LibraryConnector
import models.{APIError, DataModel}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsValue, Json, OFormat}
import play.api.mvc.Results.Status
//import play.api.mvc.Results.Status

import scala.concurrent.{ExecutionContext, Future}

class ApplicationServiceSpec extends BaseSpec with MockFactory with ScalaFutures with GuiceOneAppPerSuite{

  val mockConnector = mock[LibraryConnector]
  implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  val testService = new ApplicationService(mockConnector)

  val gameOfThrones: JsValue = Json.obj(
    "_id" -> "someId",
    "name" -> "A Game of Thrones",
    "description" -> "The best book!!!",
    "numSales" -> 100
  )

  "getGoogleBook" should {
    val url: String = "testUrl"

    "return a book" in {
      (mockConnector.get[DataModel](_: String)(_: OFormat[DataModel], _: ExecutionContext))
        .expects(url, *, *)
        .returning(EitherT.fromEither(Right(gameOfThrones.as[DataModel])))
//        .returning(Future(gameOfThrones.as[DataModel])).once() //before adding either

      whenReady(testService.getGoogleBook(urlOverride = Some(url), search = "", term = "").value) { result =>
        result shouldBe Right(DataModel("someId", "A Game of Thrones", "The best book!!!", 100))
      }
    }

    "return an error" in {
      val url: String = "testUrl"

      val errorReturned: APIError = APIError.BadAPIResponse(400, "bad req")

      (mockConnector.get[DataModel](_: String)(_: OFormat[DataModel], _: ExecutionContext))
        .expects(url, *, *)
        .returning(EitherT.fromEither(Left(errorReturned)))// How do we return an error?
        .once()

      whenReady(testService.getGoogleBook(urlOverride = Some(url), search = "", term = "").value) { result =>
        result shouldBe Left(APIError.BadAPIResponse(400, "bad req"))
      }
    }
  }
}
