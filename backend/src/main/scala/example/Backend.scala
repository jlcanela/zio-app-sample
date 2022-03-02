package example

import example.protocol.{ExampleService}
import zio._
import zio.app.DeriveRoutes

object Backend extends ZIOAppDefault {
  private val httpApp =
    DeriveRoutes.gen[ExampleService]

  val program = for {
    port <- System.envOrElse("PORT", "8088").map(_.toInt).orElseSucceed(8088)
    _    <- zhttp.service.Server.start(port, httpApp)
  } yield ()

  def run = program
      .provideCustom(ExampleServiceLive.layer)
      .exitCode
  
}

case class ExampleServiceLive(random: zio.Random) extends ExampleService {
  override def magicNumber: UIO[Int] = random.nextInt
}

object ExampleServiceLive {
  val layer = (ExampleServiceLive.apply _).toLayer[ExampleService]
}
