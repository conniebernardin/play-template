package controllers
import models.{APIError, DataModel}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.DataRepository
import services.{ApplicationService, LibraryService}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future, blocking}

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val dataRepository: DataRepository, val service: LibraryService, val applicationService: ApplicationService)(implicit val ec: ExecutionContext) extends BaseController {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    val books: Future[Seq[DataModel]] = dataRepository.collection.find().toFuture()
    books.map(items => Json.toJson(items)).map(result => Ok(result))
  }

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    applicationService.create().map {
      case Right(book: DataModel) => Created
      case Left(error) => Status(error.httpResponseStatus)(Json.toJson(error.reason))
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
  applicationService.read(id).map{
     case Right (book: DataModel) => Ok(DataModel.formats.writes(book)) //wrapped in right
     case Left(error) => Status(error.httpResponseStatus)(Json.toJson(error.reason)) //returns 500 and reason wrapped in left
   }
  }

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.update(id, dataModel).map(result => Accepted(Json.toJson(dataModel)))
      case JsError(_) => Future(BadRequest)
    }
  }

    def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
      applicationService.delete(id).map{
        case Right(message) => Accepted
        case Left(error) => Status(error.httpResponseStatus)
      }
    }

  def getGoogleBook(search: String, term: String): Action[AnyContent] = Action.async { implicit request =>
    service.getGoogleBook(search = search, term = term).value.map {
      case Right(book) => Ok(Json.toJson(book))
      case Left(error) => InternalServerError(Json.toJson(error.reason))
    }
  }
}
