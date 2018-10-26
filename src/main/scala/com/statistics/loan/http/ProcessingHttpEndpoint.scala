package com.statistics.loan.http

import cats.effect.{Effect, IO}
import com.statistics.loan.models.{CalculatedRate, Loan}
import com.statistics.loan.services.{APRService, IRRService}
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl
import cats.implicits._

class ProcessingHttpEndpoint[F[_]: Effect](aprService: APRService, irrService: IRRService)
    extends HttpEndpoint with Http4sDsl[IO] with HttpErrorHandler {
  val service: HttpService[IO] = {
    HttpService[IO] {
      case req @ POST -> Root =>
        (for {
          parsedBody <- fromBody[Loan](req)
          irr        <- irrService.calculateIRR(parsedBody)
          apr        <- aprService.calculateAPR(irr)
          res = CalculatedRate(apr, irr)
        } yield res).flatMap(x => Ok(x)).handleErrorWith(transformError)
    }
  }
}
