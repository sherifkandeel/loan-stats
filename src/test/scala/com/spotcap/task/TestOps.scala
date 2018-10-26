package com.spotcap.task
import cats.effect.IO

object TestOps {
  implicit class EntityBodyOps(entityBody: org.http4s.EntityBody[IO]) {
    def stringBody: String = new String(entityBody.compile.toVector.unsafeRunSync().toArray)
  }
}
