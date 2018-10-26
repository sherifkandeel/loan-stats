package com.statistics.loan.services

import cats.effect.IO
import com.statistics.loan.utilities.CashFlowUtility
import com.statistics.loan.models._

trait IRRService {
  def calculateIRR(loan: Loan): IO[IRR]
}

class IRRServiceImpl(cashFlowUtility: CashFlowUtility) extends IRRService {
  override def calculateIRR(loan: Loan): IO[IRR] =
    cashFlowUtility.calculateCashFlows(loan).flatMap { ls =>
      val maxIterationCount              = 20
      val absoluteAccuracy               = 1.0E-7D
      var v0                             = 0.03D
      var approximatedValue: Option[IRR] = None

      var i = 0
      while(i < maxIterationCount) {
        val values: List[Double] = ls.zipWithIndex.map { x =>
          x._1 match {
            case cf: NegativeCashFlow => cf.value * -1.0D
            case cf: PositiveCashFlow => cf.value
          }
        }

        val fValue      = values.zipWithIndex.map(x => x._1 / Math.pow(1.0D + v0, x._2)).sum
        val fDerivative = values.zipWithIndex.map(x => -1 * x._2 * x._1 / Math.pow(1.0D + v0, x._2 + 1)).sum

        val v1 = v0 - fValue / fDerivative

        if(Math.abs(v1 - v0) <= absoluteAccuracy) {
          approximatedValue = Some(IRR("%.10f".format(v1).toDouble))
        }

        v0 = v1
        i  = i + 1
      }

      approximatedValue.isEmpty match {
        case true => IO.raiseError(CalculationError.FailedToCalculateIRRNumerically)
        case _    => IO.pure(approximatedValue.get)
      }
    }

}
