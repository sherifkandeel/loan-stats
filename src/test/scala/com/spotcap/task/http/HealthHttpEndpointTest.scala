package com.spotcap.task.http

import cats.effect.IO
import com.spotcap.task.TestOps._
import org.http4s.implicits._
import org.http4s.{Method, Request, Uri, _}
import org.scalamock.specs2.MockContext
import org.specs2.mutable.Specification

class HealthHttpEndpointTest extends Specification {
  "return 200 in case server is up" in new MockContext {
    val req    = Request[IO](Method.GET, Uri.uri("/"))
    val result = new HealthHttpEndpoint[IO]().service.orNotFound(req).unsafeRunSync()

    result.status must beEqualTo(Status.Ok)
    result.body.stringBody must beEqualTo("Healthy")
  }

}
