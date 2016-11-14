package chat

import org.scalajs.dom.raw.KeyboardEvent
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.MessageEvent
import scala.scalajs.js.annotation.JSExport
import upickle.default._
import scalatags.JsDom.all._


@JSExport
object Main {

  // these need to be rendered as they will be updated by external events
  val history = div(`height`:="300px").render
  val compose = input(`class`:="form-control", placeholder:="say something").render

  // main ui element, uses bootstrap for styling
  val chatUi = div(`class`:="container")(
    div(`class`:="row")(
      div(`class`:="panel panel-default")(
        div(`class`:="panel-heading")(
          h1("Chatterbox")
        ),
        div(`class`:="panel-body")(
          history,
          div(`class`:="input-group", width:= "100%")(
            span(`class`:="input-group-addon")(">"),
            compose
          )
        )
      )
    )
  )

  // display a line in the chat box
  def display(element: Frag) = {
    val line = div(width:="100%")(element)
    history.appendChild(line.render)
  }

  @JSExport
  def main(uid: String): Unit = {
    // root element that will contain this single page app
    val root = dom.document.getElementById("root")
    while (root.firstChild != null) {
      root.removeChild(root.firstChild);
    }
    root.appendChild(chatUi.render)

    // react to messages over web socket
    val sock = new dom.WebSocket("ws://localhost:9000/socket/" + uid)
    sock.onmessage = (msg: MessageEvent) => {
      val event = read[Event](msg.data.asInstanceOf[String])
      event match {
        case Joined(uid) =>
          display(span(s"**Welcome, $uid!**\n"))
        case Message(uid, content) =>
          display(span(s"$uid: $content\n"))
        case _ =>
          dom.console.log(s"unhandled event: $event")
      }
    }

    // react to ui events
    compose.onkeypress = (ev: KeyboardEvent) => {
      if (ev.key == "Enter") {
        val msg = Broadcast(compose.value)
        dom.console.log(s"Sending $msg")
        sock.send(write(msg))
        compose.value = ""
      }
    }
  }

}
