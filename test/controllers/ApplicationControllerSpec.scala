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
    repository,
    service,
    applicationService
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

    "create a book in the database" in {

      beforeEach()

      val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED

      afterEach()
    }

//    "throw error when creating a book in database with the wrong format" in {
//
//      beforeEach()
//
//      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.obj())
//      val createdResult: Future[Result] = TestApplicationController.create()(request)
//      status(createdResult) shouldBe Status.BAD_REQUEST
//      contentAsJson(createdResult)(defaultAwaitTimeout) shouldBe Json.toJson("book could not be created")
//
//      afterEach()
//    }
  }


  "ApplicationController .read()" should {

    "find a book in the database by id" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(dataModel))
      val readRequest: FakeRequest[AnyContentAsEmpty.type] = buildGet("/api/abcd")
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      val readResult: Future[Result] = TestApplicationController.read("abcd")(readRequest)

      status(createdResult) shouldBe Status.CREATED
      status(readResult) shouldBe (Status.OK)
      contentAsJson(readResult).as[DataModel] shouldBe DataModel("abcd", "test name", "test description", 100)
      afterEach()
    }

    "throw 500 error if book cannot be found by id in database" in {
      beforeEach()
      val readRequest: FakeRequest[AnyContentAsEmpty.type] = buildGet("/api/abc")
          val readResult: Future[Result] = TestApplicationController.read("abc")(readRequest)

          status(readResult) shouldBe Status.INTERNAL_SERVER_ERROR
          afterEach()
    }
  }


  "ApplicationController .update()" should {

    "find a book in the database by id and update the fields" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val updateRequest: FakeRequest[JsValue] = buildPut("/api/update/:id").withBody[JsValue](Json.toJson(updatedDataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      val updateResult: Future[Result] = TestApplicationController.update("abcd")(updateRequest)

      status(createdResult) shouldBe Status.CREATED
      status(updateResult) shouldBe Status.ACCEPTED
      afterEach()
    }
  }

  "ApplicationController .update()" should {

    "throw an error if book is updated in wrong format" in {

    beforeEach()
    val updateRequest = buildPut("/api/4").withBody[JsValue](Json.obj())

    val updateResult = TestApplicationController.update("4")(updateRequest)

    status(updateResult) shouldBe Status.BAD_REQUEST
    afterEach()
    }
  }

  "ApplicationController .delete()" should {

    "find a book in the database by id and delete the all fields" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val deleteRequest: Request[AnyContentAsEmpty.type ] = buildDelete("/api/:id")
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      val deleteResult: Future[Result] = TestApplicationController.delete("abcd")(deleteRequest)

      status(createdResult) shouldBe Status.CREATED
      status(deleteResult) shouldBe Status.ACCEPTED
      afterEach()
    }

    "throw error if book with Id does not exist" in {
      beforeEach()
      val deleteRequest: FakeRequest[AnyContentAsEmpty.type] = buildDelete("/api/abc")
      val deleteResult: Future[Result] = TestApplicationController.delete("abc")(deleteRequest)

      status(deleteResult) shouldBe Status.INTERNAL_SERVER_ERROR
      afterEach()
    }


  }

  override def beforeEach(): Unit = repository.deleteAll()
  override def afterEach(): Unit = repository.deleteAll()
}
