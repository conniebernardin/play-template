package services

import repositories.DataRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ApplicationService @Inject() (val dataRepository: DataRepository,
                                    implicit val ec: ExecutionContext) {

}
