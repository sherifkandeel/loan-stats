package com.statistics.loan.http

import cats.effect.IO
import com.statistics.loan.models.{CalculationError, DecodingRequestError}
import org.http4s.{Response, Status}
import org.http4s.dsl.Http4sDsl

trait HttpErrorHandler {
  this: Http4sDsl[IO] =>
  val transformError: Throwable => IO[Response[IO]] = t => (handleDecodeRequestErrors orElse handleCalculationError).apply(t)

  val handleDecodeRequestErrors: PartialFunction[Throwable, IO[Response[IO]]] = {
    case DecodingRequestError.MalformedJsonError => BadRequest("Request will not be processed due to malformed JSON in payload.")
    case DecodingRequestError.InvalidJsonError   => UnprocessableEntity("Request will not be processed due to semantically invalid JSON in payload.")
  }

  val handleCalculationError: PartialFunction[Throwable, IO[Response[IO]]] = {
    case CalculationError.FailedToCalculateIRRNumerically => InternalServerError("Couldn't calculate IRR")
  }
}
