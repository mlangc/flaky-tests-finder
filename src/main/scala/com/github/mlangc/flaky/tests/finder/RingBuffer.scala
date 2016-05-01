package com.github.mlangc.flaky.tests.finder

import scala.collection.immutable.Queue

class RingBuffer[T] private (size: Int, elems: Queue[T], numElems: Int) {
  require(size >= 0, s"Negative size $size not allowed")

  def this(size: Int) = this(size, Queue[T](), 0)

  def push(t: T): RingBuffer[T] = {
    val (nElems, nNumElems) = {
      if (numElems == size) ((elems :+ t).tail, numElems)
      else (elems :+ t, numElems + 1)
    }

    new RingBuffer(size, nElems, nNumElems)
  }

  def toSeq: Seq[T] = {
    elems
  }
}
