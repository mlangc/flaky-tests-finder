package com.github.mlangc.flaky.tests.finder

import com.typesafe.scalalogging.StrictLogging

object FlakyTestsFinder extends StrictLogging {

  def main(args: Array[String]) {
    val nTimes = 25
    val buildScript = new CommandLine("/bin/bash", "runTests.bash")

    def runTests(): Set[String] = {
      val res = buildScript.exec()

      if (res.exitStatus != 0) {
        val failedTests = parseFailedTestsIn(res.lastLines)
        if (failedTests.isEmpty) {
          logger.warn("Exit status != 0 but no failed tests")
        }

        failedTests
      } else {
        Set()
      }
    }

    logger.info("Running tests (be patient)...")

    1.to(nTimes).foldLeft((Seq[String](), 0)) { case ((failedTests, failedTimes), testsRunSoFar) =>
      val res = runTests()
      val (currentFailedTests, currentFailedTimes) = if (res.nonEmpty) {
        (failedTests ++ res, failedTimes + 1)
      } else {
        (failedTests, failedTimes)
      }

      printResults(currentFailedTests, currentFailedTimes, testsRunSoFar)

      (currentFailedTests, currentFailedTimes)
    }
  }

  private def printResults(failedTests: Seq[String], failedRuns: Int, totalRuns: Int): Unit = {
    val failedTestsWithTimes = failedTests.groupBy(identity).mapValues(_.size).toSeq.map { case (test, nFailures) =>
      (nFailures, test)
    } sortBy(-_._1)

    logger.info("")
    logger.info("#" * 80)
    logger.info("#" * 80)
    logger.info("#" * 80)
    logger.info("")
    logger.info(s"Out of $totalRuns runs, the tests failed $failedRuns times")
    if (failedTestsWithTimes.nonEmpty) {
      logger.info(s"These tests failed at least once:")
      logger.info(failedTestsWithTimes.map("  " + _).mkString("\n"))
    }
    logger.info("")
  }

  private val RxTestsInError = """(Tests in error:|Failed tests:)\s*(.*)""".r

  def parseFailedTestsIn(lines: Seq[String]): Set[String] = {
    val linesFromTestsInError = lines.toSeq.dropWhile { line =>
      !line.startsWith("Tests in error:") && !line.startsWith("Failed tests:")
    }

    val testsInError = linesFromTestsInError.takeWhile { line =>
      !line.startsWith("Tests run:")
    }.map(_.trim).filterNot(_.isEmpty).flatMap { line =>
      line match {
        case RxTestsInError(_, test) => {
          if (test.nonEmpty) Some(test)
          else None
        }

        case test => Some(test)
      }
    }

    testsInError.toSet
  }
}
