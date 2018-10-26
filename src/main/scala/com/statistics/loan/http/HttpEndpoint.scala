package com.statistics.loan.http
import cats.effect.IO
import com.statistics.loan.models.DecodingRequestError
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, InvalidMessageBodyFailure, Request}


trait HttpEndpoint extends Http4sDsl[IO] {
  def fromBody[T](req: Request[IO])(implicit decoder: EntityDecoder[IO, T]): IO[T] =
    req.attemptAs[T].value.flatMap {
      case Left(_: InvalidMessageBodyFailure) => IO.raiseError(DecodingRequestError.InvalidJsonError)
      case Left(_)                            => IO.raiseError(DecodingRequestError.MalformedJsonError)
      case Right(t)                           => IO.pure(t)
    }
}
