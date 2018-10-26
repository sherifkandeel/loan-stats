package com.spotcap.task.http

import cats.effect.IO
import com.spotcap.task.TestOps._
import com.spotcap.task.models._
import com.spotcap.task.services.{APRServiceImpl, IRRServiceImpl}
import org.http4s.implicits._
import org.http4s.{Method, Request, Uri, _}
import org.scalamock.specs2.MockContext
import org.specs2.mutable.Specification
import org.http4s.circe._

class ProcessingHttpEndpointTest extends Specification {
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
  val loan = Loan(Principal(3000), UpfrontFee(10), UpfrontCreditlineFee(0), List(scheduledPayment1, scheduledPayment2, scheduledPayment3))

  "return 200 with calculated apr/irr when input is correct" in new MockContext {
    val json =
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
    val req = Request[IO](Method.POST, Uri.uri("/")).withBody(json).unsafeRunSync()

    val aprService = mock[APRServiceImpl]
    val irrService = mock[IRRServiceImpl]

    (irrService.calculateIRR _).expects(loan).returning(IO.pure(resultIRR))

    (aprService.calculateAPR _).expects(resultIRR).returning(IO.pure(resultAPR))

    val result = new ProcessingHttpEndpoint[IO](aprService, irrService).service.orNotFound(req).unsafeRunSync()

    result.status must beEqualTo(Status.Ok)
    result.body.stringBody must beEqualTo("{\"irr\":0.0066741645,\"apr\":8.3}")
  }

  "return 422 (unproccessable entity) when json is correct but not the expected form" in new MockContext {
    val json =
      """
        |{
        |  "wrong_principal": 3000,
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
    val req = Request[IO](Method.POST, Uri.uri("/")).withBody(json).unsafeRunSync()

    val aprService = mock[APRServiceImpl]
    val irrService = mock[IRRServiceImpl]

    (irrService.calculateIRR _).expects(loan).returning(IO.pure(resultIRR))

    (aprService.calculateAPR _).expects(resultIRR).returning(IO.pure(resultAPR))

    val result = new ProcessingHttpEndpoint[IO](aprService, irrService).service.orNotFound(req).unsafeRunSync()

    result.status must beEqualTo(Status.UnprocessableEntity)
    result.body.stringBody must beEqualTo("Request will not be processed due to semantically invalid JSON in payload.")

  }
  "return 400 (bad request) when the json is not correct" in new MockContext {

    val json =
      """{
        | bad_bad_json
        |}
      """.stripMargin
    val req = Request[IO](Method.POST, Uri.uri("/")).withBody(json).unsafeRunSync()

    val aprService = mock[APRServiceImpl]
    val irrService = mock[IRRServiceImpl]

    (irrService.calculateIRR _).expects(loan).returning(IO.pure(resultIRR))

    (aprService.calculateAPR _).expects(resultIRR).returning(IO.pure(resultAPR))

    val result = new ProcessingHttpEndpoint[IO](aprService, irrService).service.orNotFound(req).unsafeRunSync()

    result.status must beEqualTo(Status.BadRequest)
    result.body.stringBody must beEqualTo("Request will not be processed due to malformed JSON in payload.")
  }
}
