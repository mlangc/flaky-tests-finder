package com.github.mlangc.flaky.tests.finder

class TestFlakyTestsFinder extends UnitTest {
  "Make sure that parsing failed tests out of command line output works" in {
    def testParseFailedTests(name: String, catchwords: Set[String]): Unit = {
      val results = FlakyTestsFinder.parseFailedTestsIn(testResourceLines(name))
      val expectedResults = catchwords.flatMap { catchword =>
        results.find(_.contains(catchword))
      }

      results.diff(expectedResults) should be (empty)
      expectedResults.diff(results) should be (empty)
    }

    testParseFailedTests("testsOkOut.txt", Set())
    testParseFailedTests("testsNokOut1.txt", Set("BitwiseXorTest"))
    testParseFailedTests("testsNokOut2.txt", Set("BitwiseAndTest"))
    testParseFailedTests("testsNokOut3.txt", Set("MethodsAsFunctionsInnerObjectTest"))

    testParseFailedTests("testsNokOut4.txt", Set(
        "noAccessToPrivateThisOutsideOfInstance",
        "noAccessToPrivateInSubclass",
        "accessToPrivateThisInsideOfInstance",
        "noAccessToPrivateInUnrelatedClass"))
  }
}
