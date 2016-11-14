A simple chat application that demonstrates integration of Play with ScalaJS.

## Run

1. start sbt: `sbt`
2. compile on demand, when files change: `run`
3. go to `http://localhost:9000/chat/USERNAME` where `USERNAME` is your name of choice

## Important directories

- `server/` contains the Play server that will serve the client app
- `client/` ScalaJS client app sources
- `shared/` common sources, available in both server and client. In this project, shared contains messages that are passed between the server and client via a websocket

## References
- [Play framework](https://www.playframework.com)
- [ScalaJS](https://www.scala-js.org/)
- [Play-ScalaJS integration](https://github.com/vmunier/play-with-scalajs-example)
