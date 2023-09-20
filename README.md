<<<<<<< HEAD
# Play samples

[![Twitter Follow](https://img.shields.io/twitter/follow/playframework?label=follow&style=flat&logo=twitter&color=brightgreen)](https://twitter.com/playframework)
[![Discord](https://img.shields.io/discord/931647755942776882?logo=discord&logoColor=white)](https://discord.gg/g5s2vtZ4Fa)
[![GitHub Discussions](https://img.shields.io/github/discussions/playframework/playframework?&logo=github&color=brightgreen)](https://github.com/playframework/playframework/discussions)
[![StackOverflow](https://img.shields.io/static/v1?label=stackoverflow&logo=stackoverflow&logoColor=fe7a16&color=brightgreen&message=playframework)](https://stackoverflow.com/tags/playframework)
[![YouTube](https://img.shields.io/youtube/channel/views/UCRp6QDm5SDjbIuisUpxV9cg?label=watch&logo=youtube&style=flat&color=brightgreen&logoColor=ff0000)](https://www.youtube.com/channel/UCRp6QDm5SDjbIuisUpxV9cg)
[![Twitch Status](https://img.shields.io/twitch/status/playframework?logo=twitch&logoColor=white&color=brightgreen&label=live%20stream)](https://www.twitch.tv/playframework)
[![OpenCollective](https://img.shields.io/opencollective/all/playframework?label=financial%20contributors&logo=open-collective)](https://opencollective.com/playframework)

[![Build Status](https://github.com/playframework/play-samples/actions/workflows/build-test.yml/badge.svg)](https://github.com/playframework/play-samples/actions/workflows/build-test.yml)
[![Repository size](https://img.shields.io/github/repo-size/playframework/play-samples.svg?logo=git)](https://github.com/playframework/play-samples)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![Mergify Status](https://img.shields.io/endpoint.svg?url=https://api.mergify.com/v1/badges/playframework/play-samples&style=flat)](https://mergify.com)

## License

To the extent possible under law, the author(s) have dedicated all copyright and
related and neighboring rights to this software to the public domain worldwide.
This software is distributed without any warranty.

Unless you explicitly state otherwise, any contribution intentionally submitted
for inclusion by you shall be licensed as above, without any additional terms
or conditions.
=======
# play-scala-chatroom-example

This is a simple chatroom using Play and Websockets with the Scala API.

This project makes use of [dynamic streams](http://doc.akka.io/docs/akka/2.6/scala/stream/stream-dynamic.html) from Akka Streams, notably `BroadcastHub` and `MergeHub`.  By [combining MergeHub and BroadcastHub](https://doc.akka.io/docs/akka/2.6/stream/stream-dynamic.html?language=scala#dynamic-fan-in-and-fan-out-with-mergehub-broadcasthub-and-partitionhub), you can get publish/subscribe functionality.

## The good bit

The flow is defined once in the controller, and used everywhere from the `chat` action:

```scala
import javax.inject._
import play.api.mvc._

import akka.stream.scaladsl._
import scala.concurrent._

class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

    private type WSMessage = String

  // chat room many clients -> merge hub -> broadcasthub -> many clients
  private val (chatSink, chatSource) = {

    // Don't log MergeHub$ProducerFailed as error if the client disconnects.
    // recoverWithRetries -1 is essentially "recoverWith"
    val source = MergeHub.source[WSMessage]
      .log("source")
      .recoverWithRetries(-1, { case _: Exception => Source.empty })

    val sink = BroadcastHub.sink[WSMessage]
    source.toMat(sink)(Keep.both).run()
  }

  private val userFlow: Flow[WSMessage, WSMessage, _] = {
    Flow[WSMessage].via(Flow.fromSinkAndSource(chatSink, chatSource)).log("userFlow")
  }

  def chat: WebSocket = {
    WebSocket.acceptOrResult[WSMessage, WSMessage] {
      case rh if sameOriginCheck(rh) =>
        Future.successful(userFlow).map { flow =>
          Right(flow)
        }.recover {
          case e: Exception =>
            Left(InternalServerError("Cannot create websocket"))
        }

      case rejected =>
        Future.successful {
          Left(Forbidden("forbidden"))
        }
    }
  }
}
```

## Prerequisites

You will need [JDK 11](https://adoptopenjdk.net/) and [sbt](http://www.scala-sbt.org/) installed.

## Running

```bash
sbt run
```

Go to <http://localhost:9000> and open it in two different browsers.  Typing into one browser will cause it to show up in another browser.

## Tributes

This project is originally taken from Johan Andrén's [Akka-HTTP version](https://github.com/johanandren/chat-with-akka-http-websockets/tree/akka-2.4.10):

Johan also has a blog post explaining dynamic streams in more detail:

* <http://markatta.com/codemonkey/blog/2016/10/02/chat-with-akka-http-websockets/>
>>>>>>> dd7d5f04 (first commit)
