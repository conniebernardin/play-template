package services

import baseSpec.{BaseSpec, BaseSpecWithApplication}
import models.DataModel
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, status}
import repositories.MockRepositoryTrait

import scala.concurrent.{ExecutionContext, Future}


 class ApplicationServiceSpec extends BaseSpecWithApplication with MockFactory with ScalaFutures {

  val mockedDataRepo: MockRepositoryTrait = mock[MockRepositoryTrait]
  val applicationServiceTest = new ApplicationService(mockedDataRepo)


  val bookExample: JsValue = Json.obj(
    "_id" -> "Book Id",
    "name" -> "Book Name",
    "description" -> "The best book!!!",
    "numSales" -> 1000
  )


  "ApplicationService.read ss" should {

    val id = "Book Id"

    "return a book when searched by id" in {
      beforeEach()
      (mockedDataRepo.read(_: String))
        .expects(id)
        .returning(Future(Right(bookExample.as[DataModel])))

      whenReady(applicationServiceTest.read(id)) { result =>
        result shouldBe Right(DataModel("Book Id", "Book Name", "The best book!!!", 1000))
      }
      afterEach()
    }

     def beforeEach(): Unit = repository.deleteAll()
     def afterEach(): Unit = repository.deleteAll()

  }
}