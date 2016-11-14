package chat

sealed trait Command // message to chat server
sealed trait Event // message from chat server

case class Broadcast(content: String) extends Command

case class Joined(uid: String) extends Event
case class Left(uid: String) extends Event
case class Message(uid: String, content: String) extends Event
