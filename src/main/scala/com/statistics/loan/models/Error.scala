package com.statistics.loan.models

class NoStackTraceThrowable(message: String) extends Throwable(message, null, false, false) {
  def this() = this(null)
}

sealed trait DecodingRequestError extends NoStackTraceThrowable with Product with Serializable
object DecodingRequestError {
  final case object MalformedJsonError extends DecodingRequestError
  final case object InvalidJsonError   extends DecodingRequestError
}

sealed trait CalculationError extends NoStackTraceThrowable with Product with Serializable
object CalculationError {
  final case object FailedToCalculateIRRNumerically extends CalculationError
}
