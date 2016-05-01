package com.github.mlangc.flaky.tests.finder

class TestRingBuffer extends UnitTest {
  "A buffer with size 1 should behave properly" in {
    val buf = new RingBuffer[Int](1)

    buf.toSeq should be (empty)
    buf.push(0).toSeq should be (Seq(0))
    buf.push(0).push(1).toSeq should be (Seq(1))
  }

  "A buffer with size 0 should behave properly" in {
    val buf = new RingBuffer[Int](0)
    buf.push(1).push(2).toSeq should be (empty)
  }

  "A buffer must not have a negative size" in {
    an[IllegalArgumentException] should be thrownBy(new RingBuffer[Int](-1))
  }

  "A typical buffer should behave properly" in {
    val buf = new RingBuffer[Int](3)
    buf.toSeq should be (empty)
    buf.push(1).push(2).toSeq should be (Seq(1, 2))
    buf.push(1).push(2).push(3).toSeq should be (Seq(1, 2, 3))
    buf.push(1).push(2).push(3).push(4).push(5).toSeq should be (Seq(3, 4, 5))
  }
}
