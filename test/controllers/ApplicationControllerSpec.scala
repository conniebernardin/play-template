package controllers

import baseSpec.BaseSpecWithApplication
import models.DataModel
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AnyContent, AnyContentAsEmpty, Request, Result}
import play.api.test.Helpers.{contentAsJson, defaultAwaitTimeout, status}

import scala.concurrent.Future
import scala.reflect.internal.NoPhase

class ApplicationControllerSpec extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(
    component,
    repository
  )

  private val dataModel: DataModel = DataModel(
    "abcd",
    "test name",
    "test description",
    100
  )

  private val updatedDataModel: DataModel = DataModel(
    "abcd",
    "updated test name",
    "test description",
    100
  )

  "ApplicationController .index" should {
    beforeEach()
    val result = TestApplicationController.index()(FakeRequest())

    "return TODO" in {
      status(result) shouldBe Status.OK
    }
    afterEach()
  }




  "ApplicationController .create" should {
beforeEach()
    "create a book in the database" in {

      val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED
    }
    afterEach()
  }

  "ApplicationController .create" should {
    beforeEach()
    val request = buildPost("/api/create").withBody[JsValue](Json.obj())
    val createdResult = TestApplicationController.create()(request)

    "throw error when creating a book in database with the wrong format" in {
      status(createdResult) shouldBe Status.BAD_REQUEST
    }
    afterEach()
  }


  "ApplicationController .read()" should {
    beforeEach()
    "find a book in the database by id" in {

      val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(dataModel))

      val createdResult: Future[Result] = TestApplicationController.create()(request)

      val readResult: Future[Result] = TestApplicationController.read("abcd")(FakeRequest())

      status(createdResult) shouldBe Status.CREATED
      status(readResult) shouldBe Status.OK
      contentAsJson(readResult).as[DataModel] shouldBe DataModel("abcd", "test name", "test description", 100)

    }
    afterEach()
  }

  "ApplicationController .update()" should {
    beforeEach()
    "find a book in the database by id and update the fields" in {

      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val updateRequest: FakeRequest[JsValue] = buildPut("/api/update/:id").withBody[JsValue](Json.toJson(updatedDataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      val updateResult: Future[Result] = TestApplicationController.update("abcd")(updateRequest)

      status(createdResult) shouldBe Status.CREATED
      status(updateResult) shouldBe Status.ACCEPTED

    }
    afterEach()
  }

  "ApplicationController .delete()" should {
    beforeEach()
    "find a book in the database by id and delete the all fields" in {
      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val deleteRequest: Request[AnyContentAsEmpty.type ] = buildDelete("/api/:id")
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      val deleteResult: Future[Result] = TestApplicationController.delete("abcd")(deleteRequest)

      status(createdResult) shouldBe Status.CREATED
      status(deleteResult) shouldBe Status.ACCEPTED

    }
    afterEach()

  }

  override def beforeEach(): Unit = repository.deleteAll()
  override def afterEach(): Unit = repository.deleteAll()
}
