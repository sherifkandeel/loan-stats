package com.statistics.loan.models

sealed trait CashFlow
case class NegativeCashFlow(value: Double) extends CashFlow
case class PositiveCashFlow(value: Double) extends CashFlow
