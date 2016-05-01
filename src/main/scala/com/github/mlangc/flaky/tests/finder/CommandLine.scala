package com.github.mlangc.flaky.tests.finder

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream
import util.control.Breaks._
import com.typesafe.scalalogging.StrictLogging

class CommandLine(command: String*) extends StrictLogging {
  import CommandLine._

  private val processBuilder = new ProcessBuilder(command: _*)

  def exec(keepLines: Int = 1024): Result  = {
    val process = processBuilder.start()
    val in = new BufferedReader(new InputStreamReader(process.getInputStream))
    val err = new BufferedReader(new InputStreamReader(process.getErrorStream))

    var linesRead = 0
    var lastLines = new RingBuffer[String](keepLines)
    var tsLastInfoMsg = System.currentTimeMillis()

    def readLine(reader: BufferedReader, out: PrintStream, depleted: Boolean, onDepleted: => Unit): Boolean = {
      if (!depleted && reader.ready()) {
        val line = reader.readLine()
        if (line == null) {
          onDepleted
          true
        } else {
          linesRead += 1
          lastLines = lastLines.push(line)

          if (logger.underlying.isDebugEnabled()) {
            val prefix = if (out == System.out) "OUT> " else "ERR> "
            logger.debug(prefix + line)
          } else if (linesRead % 64 == 0) {
            val now = System.currentTimeMillis()
            if (now - tsLastInfoMsg > 30 * 1000) {
              logger.info(s"Reading lines ($linesRead already read)...")
              tsLastInfoMsg = now
            }
          }

          true
        }
      } else {
        false
      }
    }

    var inDepleted = false
    var errDepleted = false
    var readFromZombie = false

    breakable {
      while (true) {
        val readFromIn = readLine(in, System.out, inDepleted, inDepleted = true)
        val readFromErr = readLine(err, System.err, errDepleted, errDepleted = true)

        if (inDepleted && errDepleted) {
          break
        }

        if (!readFromIn && !readFromErr) {
          if (readFromZombie) {
            break
          } else if (!process.isAlive()) {
            readFromZombie = true
          }

          Thread.sleep(1)
        }
      }
    }

    Result(process.waitFor(), lastLines.toSeq)
  }
}

object CommandLine {
  case class Result(exitStatus: Int, lastLines: Seq[String])
}
