package chat

import scala.scalajs.js

import org.scalajs.dom
import scala.util.{ Failure, Success }
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import upickle.default._

object Main extends js.JSApp {

  def main(): Unit = {
    val root = dom.document.getElementById("root")

    dom.ext.Ajax.get("/message").onComplete {
      case Success(msg) if 200 <= msg.status && msg.status < 300 =>
        root.textContent = "OK, " + read[Message](msg.responseText).data
      case Success(msg) =>
        root.textContent = msg.responseText
      case Failure(err) =>
        root.textContent = "ERROR: " + err
    }
    //dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
  }

}
