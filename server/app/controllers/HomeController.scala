package controllers

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import chat._
import javax.inject._
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import play.api.mvc.WebSocket.MessageFlowTransformer
import upickle.default._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
class HomeController @Inject() (
  implicit system: ActorSystem,
  mat: Materializer
) extends Controller {

  lazy val room: ActorRef = system.actorOf(Props(classOf[RoomActor]))

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

  def socket(uid: String) = WebSocket.accept[Command, Event] { request =>
    ActorFlow.actorRef(out => Props(classOf[ClientActor], uid, room, out))
  }

  implicit val transformer = MessageFlowTransformer.stringMessageFlowTransformer.map(
    in => read[Command](in),
    (out: Event) => write(out)
  )

}
