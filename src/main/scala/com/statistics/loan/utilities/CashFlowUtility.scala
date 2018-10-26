package com.statistics.loan.utilities
import cats.effect.IO
import com.statistics.loan.models.{CashFlow, Loan, NegativeCashFlow, PositiveCashFlow}

trait CashFlowUtility {
  def calculateCashFlows(loan: Loan): IO[List[CashFlow]]
}

class CashFlowUtilityImpl extends CashFlowUtility {
  override def calculateCashFlows(loan: Loan): IO[List[CashFlow]] = {
    val initialFlow = NegativeCashFlow(loan.principal.value - loan.upfrontFee.value - loan.upfrontCreditlineFee.value)
    val schedules   = loan.scheduledPayments.map(p => PositiveCashFlow(p.principal.value + p.interestFee.value))
    IO.pure(initialFlow :: schedules)
  }
}
