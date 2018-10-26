package com.statistics.loan.models

import cats.effect.IO
import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe._
import io.circe.literal._

case class CalculatedRate(apr: APR, irr: IRR)

case class APR(value: Double) extends AnyVal

case class IRR(value: Double) extends AnyVal

object CalculatedRate {
  implicit val encoder: Encoder[CalculatedRate] = Encoder.instance { calculatedRate: CalculatedRate =>
      json"""{
            "irr": ${calculatedRate.irr.value},
            "apr": ${calculatedRate.apr.value}
            }"""
    }
  implicit val entityEncoder: EntityEncoder[IO, CalculatedRate] = jsonEncoderOf[IO, CalculatedRate]

}

