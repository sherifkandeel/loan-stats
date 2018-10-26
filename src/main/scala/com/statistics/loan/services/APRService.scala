package com.statistics.loan.services

import cats.effect.IO
import com.statistics.loan.models.{APR, IRR, ScheduledPayment}
import scala.math._

trait APRService {
  def calculateAPR(irr: IRR): IO[APR]
}
class APRServiceImpl extends APRService {
  override def calculateAPR(irr: IRR): IO[APR] = {
    val apr = (Math.pow(1.0D + irr.value, 12.0D) - 1.0D) * 100.0D
    IO.pure(APR("%.1f".format(apr).toDouble))
  }

}
