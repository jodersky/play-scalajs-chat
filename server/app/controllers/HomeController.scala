package controllers

import play.api._
import play.api.mvc._
import upickle.default._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
class HomeController extends Controller {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def message = Action { implicit request =>
    Ok(write(chat.Message("hello")))
  }

}
