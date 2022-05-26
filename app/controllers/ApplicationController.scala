package controllers
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController{

  def index() = TODO

}
