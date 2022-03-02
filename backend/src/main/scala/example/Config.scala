package example

import zio._
import zio.config._
import zio.config.magnolia._
import zio.config.typesafe._

import java.io.File

case class Config(usernames: List[String])

object Config {
  val descriptor: ConfigDescriptor[Config] =
    DeriveConfigDescriptor.descriptor[Config]

  val live = //: ZLayer[System, Nothing, Config] =
    TypesafeConfig.fromHoconFile(new File("application.conf"), descriptor).orDie

  val service: URIO[Config, Config] = ZIO.service[Config]
}
