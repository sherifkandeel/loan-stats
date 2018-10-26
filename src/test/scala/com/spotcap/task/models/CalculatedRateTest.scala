package com.spotcap.task.models

import io.circe.Json
import org.scalamock.specs2.MockContext
import org.specs2.mutable.Specification
import io.circe.parser._

class CalculatedRateTest extends Specification {
  val apr            = APR(8.3D)
  val irr            = IRR(0.0066741645D)
  val calculatedRate = CalculatedRate(apr, irr)
  "CalculatedRateTest" should {
    "Correctly encode the object into correct json" in new MockContext {
      CalculatedRate.encoder.apply(calculatedRate) must beEqualTo(
        parse("""
          |{
          |    "irr": 0.0066741645,
          |    "apr": 8.3
          |}""".stripMargin).getOrElse(Json.Null)
      )

    }
  }
}
