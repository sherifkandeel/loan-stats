package com.statistics.loan.models

import cats.effect.IO
import cats.implicits._
import io.circe.{Decoder, HCursor}
import org.http4s.circe._
import org.http4s.EntityDecoder

case class Principal(value: Double)            extends AnyVal
case class UpfrontFee(value: Double)           extends AnyVal
case class UpfrontCreditlineFee(value: Double) extends AnyVal
case class Loan(principal: Principal, upfrontFee: UpfrontFee, upfrontCreditlineFee: UpfrontCreditlineFee, scheduledPayments: List[ScheduledPayment])
object Loan {
  import ScheduledPayment._
  implicit val decoder: Decoder[Loan] = (c: HCursor) =>
    (
      c.downField("principal").as[Double].map(Principal),
      c.downField("upfrontFee").as[UpfrontFee],
      c.downField("upfrontCreditlineFee").as[UpfrontCreditlineFee],
      c.downField("schedule").as[List[ScheduledPayment]]).mapN(Loan.apply)
  implicit val entityDecoder: EntityDecoder[IO, Loan] = jsonOf[IO, Loan]

  implicit val upFrontFeeDecoder: Decoder[UpfrontFee]                 = (c: HCursor) => c.downField("value").as[Double].map(UpfrontFee)
  implicit val upFrontFeeEntityDecoder: EntityDecoder[IO, UpfrontFee] = jsonOf[IO, UpfrontFee]

  implicit val UpfrontCreditlineFeeDecoder: Decoder[UpfrontCreditlineFee]                 = (c: HCursor) => c.downField("value").as[Double].map(UpfrontCreditlineFee)
  implicit val UpfrontCreditlineFeeEntityDecoder: EntityDecoder[IO, UpfrontCreditlineFee] = jsonOf[IO, UpfrontCreditlineFee]
}
