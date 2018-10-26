package com.spotcap.task.models

import org.scalamock.specs2.MockContext
import org.specs2.mutable.Specification
import io.circe.parser._
import io.circe.Json

class LoanTest extends Specification {
  val resultIRR = IRR(0.0066741645D)
  val resultAPR = APR(8.3D)
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
  val loan      = Loan(Principal(3000), UpfrontFee(10), UpfrontCreditlineFee(0), List(scheduledPayment1, scheduledPayment2, scheduledPayment3))
  val wrongJson = parse("""{"wrong_key": "wrong_value" }""".stripMargin).getOrElse(Json.Null)
  val json = parse(
    """
        |{
        |  "principal": 3000,
        |  "upfrontFee": {
        |    "value": 10
        |  },
        |  "upfrontCreditlineFee": {
        |    "value": 0
        |  },
        |  "schedule": [
        |    {
        |      "id": 1,
        |      "date": "2016-10-20",
        |      "principal": 1000,
        |      "interestFee": 10
        |    },
        |    {
        |      "id": 2,
        |      "date": "2016-11-21",
        |      "principal": 1000,
        |      "interestFee": 10
        |    },
        |    {
        |      "id": 3,
        |      "date": "2016-12-20",
        |      "principal": 1000,
        |      "interestFee": 10
        |    }
        |  ]
        |}
      """.stripMargin
  ).getOrElse(Json.Null)

  "LoanTest" should {
    "Correctly decode the object into correct json" in {
      Loan.decoder.decodeJson(json).toOption must beEqualTo(Some(loan))
    }

    "Does not decode wrong json" in {
      Loan.decoder.decodeJson(wrongJson).toOption must beEqualTo(None)
    }
  }
}
