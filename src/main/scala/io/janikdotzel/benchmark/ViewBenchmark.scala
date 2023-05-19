package io.janikdotzel.benchmark

import org.scalameter.api._

object ViewVsList extends Bench.LocalTime {

  val sizes = Gen.range("size")(10000, 500000, 10000)
  val lists = sizes.map(size => (0 until size).toList)
  val views = sizes.map(size => (0 until size).view)

  performance of "List" in {
    measure method "map" in {
      using(lists) in { xs =>
        xs.map(_ + 1)
      }
    }
  }

  performance of "View" in {
    measure method "map" in {
      using(views) in { xs =>
        xs.map(_ + 1).force
      }
    }
  }
}

