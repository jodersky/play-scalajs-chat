package controllers

import akka.actor.{ Actor, ActorRef, Terminated }
import chat._
import scala.collection.mutable.HashMap


/** An actor instantiated for every websocket connection. It represents a
  * chat client and registers with a chat room.
  * @param uid user id of the connecting client
  * @param room chat room actor that this client will join
  * @param socket websocket actor, any message sent to it will get transferred to the remote
  * browser
  */
class ClientActor(uid: String, room: ActorRef, socket: ActorRef) extends Actor {

  override def preStart() = {
    room ! (self, uid)
  }

  def receive = {
    case cmd: Command => room ! cmd
    case ev: Event => socket ! ev
  }

}

/** An actor that represents a chat room.
  * Handles commands and events subclassing `chat.Command` and `chat.Event`
  */
class RoomActor extends Actor {
  val clients = new HashMap[ActorRef, String]

  override def receive = {

    case (client: ActorRef, uid: String) =>
      context.watch(sender)
      clients += client -> uid
      for ( (client, _) <- clients ) {
        client ! Joined(uid)
      }

    case Terminated(client) =>
      clients -= client
      for ( (cl, _) <- clients ) {
        cl ! Left(clients(client))
      }

    case Broadcast(content) =>
      val origin = clients(sender)
      for ( (client, _) <- clients ) {
        client ! Message(origin, content)
      }
  }
}

