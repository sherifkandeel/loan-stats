package com.statistics.loan

import cats.effect.{Effect, IO}
import com.statistics.loan.utilities.CashFlowUtilityImpl
import com.statistics.loan.http.{HealthHttpEndpoint, ProcessingHttpEndpoint}
import com.statistics.loan.services.{APRService, APRServiceImpl, IRRService, IRRServiceImpl}
import fs2.StreamApp
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext

object ApplicationServer extends StreamApp[IO] with Http4sDsl[IO] {
  import scala.concurrent.ExecutionContext.Implicits.global
  def stream(args: List[String], requestShutdown: IO[Unit]) = ServerStream.stream[IO]
}

object ServerStream {

  def cashFlowUtility = new CashFlowUtilityImpl()
  def aprService = new APRServiceImpl()
  def irrService = new IRRServiceImpl(cashFlowUtility)
  def processingHttpService[F[_]: Effect] = new ProcessingHttpEndpoint[IO](aprService: APRService, irrService: IRRService).service
  def healthCheckHttpService[F[_]: Effect] = new HealthHttpEndpoint[IO].service

  def stream[F[_]: Effect](implicit ec: ExecutionContext) =
    BlazeBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .mountService(healthCheckHttpService, "/health")
      .mountService(processingHttpService, "/calculation")
      .serve
}
