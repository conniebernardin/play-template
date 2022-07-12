package services

import baseSpec.{BaseSpec, BaseSpecWithApplication}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import repositories.MockRepositoryTrait


class ApplicationServiceSpec extends BaseSpecWithApplication with MockFactory with ScalaFutures with GuiceOneAppPerSuite{

  val mockedDataRepo: MockRepositoryTrait = mock[MockRepositoryTrait]
  val applicationServiceTest = new ApplicationService(mockedDataRepo)

}
