package com.github.mlangc.flaky.tests.finder

import org.scalatest.OptionValues
import org.scalatest.FreeSpec
import org.scalatest.Matchers
import scala.io.Source

abstract class UnitTest extends FreeSpec with OptionValues with Matchers {
  protected final def testResourceLines(name: String): Seq[String] = {
    Option(getClass.getResource(name)).map { url =>
      val source = Source.fromURL(url, "UTF-8")
      try {
        source.getLines().toVector
      } finally {
        source.close()
      }
    }.getOrElse {
      sys.error(s"Cannot load resource $name relative to ${getClass.getPackage}")
    }
  }
}
