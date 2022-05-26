package controllers
import play.api.mvc.{BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController

