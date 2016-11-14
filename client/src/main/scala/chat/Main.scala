package chat

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js

import org.scalajs.dom
import org.scalajs.dom.raw.MessageEvent
import upickle.default._

object Main extends js.JSApp {

  def main(): Unit = {
    val root = dom.document.getElementById("root")
    val sock = new dom.WebSocket("ws://localhost:9000/socket/john")

    sock.onmessage = (msg: MessageEvent) => {
      val event = read[Event](msg.data.asInstanceOf[String])
      dom.console.log(event.toString())
    }
  }

}
