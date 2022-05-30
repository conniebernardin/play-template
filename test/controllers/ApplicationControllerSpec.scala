package controllers

import baseSpec.BaseSpecWithApplication
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.test.Helpers.{defaultAwaitTimeout, status}

import scala.reflect.internal.NoPhase

class ApplicationControllerSpec extends BaseSpecWithApplication{

  val TestApplicationController = new ApplicationController(
    component)

  "ApplicationController .index" should {

    val result = TestApplicationController.index()(FakeRequest())

    "return TODO" in {
      status(result) shouldBe Status.NOT_IMPLEMENTED
    }
  }

  "ApplicationController .create()" should {

  }

  "ApplicationController .read()" should {
    val result = TestApplicationController.read(String)(FakeRequest())
    "return TODO" in {
      status(result) shouldBe Status.OK
    }
  }

  "ApplicationController .update()" should {

  }

  "ApplicationController .delete()" should {

  }
}
