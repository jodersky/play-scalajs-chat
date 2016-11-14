package chat

import org.scalajs.dom.raw.KeyboardEvent
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js

import org.scalajs.dom
import org.scalajs.dom.raw.MessageEvent
import scala.scalajs.js.annotation.JSExport
import upickle.default._
import scalatags.JsDom.all._


@JSExport
object Main {

  @JSExport
  def main(uid: String): Unit = {
    val root = dom.document.getElementById("root")
    while (root.firstChild != null) {
      root.removeChild(root.firstChild);
    }
    
    val history = textarea().render
    val compose = input().render
    root.appendChild(history)
    root.appendChild(compose)

    val sock = new dom.WebSocket("ws://localhost:9000/socket/" + uid)
    sock.onmessage = (msg: MessageEvent) => {
      val event = read[Event](msg.data.asInstanceOf[String])
      event match {
        case Joined(uid) =>
          history.textContent += s"**$uid joined**\n"
        case Message(uid, content) =>
          history.textContent += s"$uid: $content\n"
        case _ =>
          dom.console.log(s"unhandled event: $event")
      }
    }

    compose.onkeypress = (ev: KeyboardEvent) => {
      if (ev.key == "Enter") {
        val msg = Broadcast(compose.value)
        dom.console.log(s"Sending $msg")
        sock.send(write(msg))
      }
    }

  }

}
