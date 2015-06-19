package com.seanshubin.devon.core.devon

import java.time.{Instant, ZonedDateTime}

case class SampleWithTopLevelTypes(sampleString: String,
                                   sampleBigInt: BigInt,
                                   sampleBigDecimal: BigDecimal,
                                   sampleZonedDateTime: ZonedDateTime,
                                   sampleInstant: Instant)
