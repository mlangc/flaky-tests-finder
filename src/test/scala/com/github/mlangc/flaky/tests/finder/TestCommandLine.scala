package com.github.mlangc.flaky.tests.finder

class TestCommandLine extends UnitTest {
  "Test our command line with" - {
    "ls -l" in {
      val ls = new CommandLine("ls", "-l", "/")
      val res = ls.exec(keepLines = 2)
      res.exitStatus should be (0)
      res.lastLines should have size (2)
    }

    "ls -b0rkage" in {
      val ls = new CommandLine("ls", "-b0rkage")
      val res = ls.exec()
      res.exitStatus should be (2)
      res.lastLines should not be (empty)
    }

    "echo" in {
      val echo = new CommandLine("echo", "-e", """0\n1\n2\n3\n4""")
      val res = echo.exec(keepLines = 3)
      res.exitStatus should be (0)
      res.lastLines should be (Seq("2", "3", "4"))
    }
  }
}
