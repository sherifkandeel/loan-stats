package com.statistics.loan.http

import cats.effect.{Effect, IO}
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

class HealthHttpEndpoint[F[_]: Effect] extends Http4sDsl[IO] {

  val service: HttpService[IO] = HttpService[IO] {
    case GET -> Root => Ok("Healthy")
  }

}
