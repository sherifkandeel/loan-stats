package com.spotcap.task.models

import io.circe.parser._
import io.circe.Json
import org.scalamock.specs2.MockContext
import org.specs2.mutable.Specification

class ScheduledPaymentTest extends Specification {
  val scheduledPayment = ScheduledPayment(
    ScheduledPaymentId(1),
    ScheduledPaymentDate("2016-10-20"),
    ScheduledPaymentPrincipal(1000.1234),
    ScheduledPaymentInterestFee(101.234)
  )

  val json = parse(
    """
      |    {
      |      "id": 1,
      |      "date": "2016-10-20",
      |      "principal": 1000.1234,
      |      "interestFee": 101.234
      |    }
    """.stripMargin
  ).getOrElse(Json.Null)

  "ScheduledPaymentTest" should {
    "Correctly decode the object into correct json" in new MockContext {
      ScheduledPayment.decoder.decodeJson(json).toOption must beEqualTo(Some(scheduledPayment))
    }

  }
}
