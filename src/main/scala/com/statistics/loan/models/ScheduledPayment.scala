package com.statistics.loan.models

import cats.effect.IO
import cats.implicits._
import io.circe.{Decoder, HCursor}
import org.http4s.circe._
import org.http4s.EntityDecoder

case class ScheduledPaymentId(value: Long)            extends AnyVal
case class ScheduledPaymentDate(value: String)        extends AnyVal
case class ScheduledPaymentPrincipal(value: Double)   extends AnyVal
case class ScheduledPaymentInterestFee(value: Double) extends AnyVal
case class ScheduledPayment(
    id: ScheduledPaymentId,
    date: ScheduledPaymentDate,
    principal: ScheduledPaymentPrincipal,
    interestFee: ScheduledPaymentInterestFee)

object ScheduledPayment {
  implicit val decoder: Decoder[ScheduledPayment] = (c: HCursor) =>
    (
      c.downField("id").as[Long].map(ScheduledPaymentId),
      c.downField("date").as[String].map(ScheduledPaymentDate),
      c.downField("principal").as[Double].map(ScheduledPaymentPrincipal),
      c.downField("interestFee").as[Double].map(ScheduledPaymentInterestFee)
    ).mapN(ScheduledPayment.apply)
  implicit val entityDecoder: EntityDecoder[IO, ScheduledPayment] = jsonOf[IO, ScheduledPayment]

}
