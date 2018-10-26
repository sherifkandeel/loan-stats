package com.spotcap.task.utilities

import com.spotcap.task.models._
import org.specs2.mutable.Specification

class CashFlowUtilityImplTest extends Specification {
  val scheduledPayment1 = ScheduledPayment(
    ScheduledPaymentId(1),
    ScheduledPaymentDate("2016-10-20"),
    ScheduledPaymentPrincipal(1000),
    ScheduledPaymentInterestFee(10)
  )
  val scheduledPayment2 = ScheduledPayment(
    ScheduledPaymentId(2),
    ScheduledPaymentDate("2016-11-21"),
    ScheduledPaymentPrincipal(1000),
    ScheduledPaymentInterestFee(10)
  )
  val scheduledPayment3 = ScheduledPayment(
    ScheduledPaymentId(3),
    ScheduledPaymentDate("2016-12-20"),
    ScheduledPaymentPrincipal(1000),
    ScheduledPaymentInterestFee(10)
  )
  val loan = Loan(Principal(3000), UpfrontFee(10), UpfrontCreditlineFee(0), List(scheduledPayment1, scheduledPayment2, scheduledPayment3))

  val cashFlowList = List(NegativeCashFlow(2990), PositiveCashFlow(1010), PositiveCashFlow(1010), PositiveCashFlow(1010))
  "CashFlowUtilityImplTest" should {
    "calculateCashFlows" in {
      val cashFlowUtility = new CashFlowUtilityImpl()
      cashFlowUtility.calculateCashFlows(loan).unsafeRunSync() must beEqualTo(cashFlowList)
    }

  }
}
