package com.spotcap.task.services

import com.spotcap.task.models.{APR, IRR}
import org.scalamock.specs2.MockContext
import org.specs2.mutable.Specification

class APRServiceImplTest extends Specification {
  "calculateAPR" should {
    "Correctly calculate APR given IRR" in {
      val aprService = new APRServiceImpl()
      aprService.calculateAPR(IRR(0.0334008783D)).unsafeRunSync() must beEqualTo(APR(48.3D))
      aprService.calculateAPR(IRR(0.053D)).unsafeRunSync() must beEqualTo(APR(85.8D))
      aprService.calculateAPR(IRR(0.1D)).unsafeRunSync() must beEqualTo(APR(213.8D))
      aprService.calculateAPR(IRR(-0.1D)).unsafeRunSync() must beEqualTo(APR(-71.8D))
    }
  }
}
