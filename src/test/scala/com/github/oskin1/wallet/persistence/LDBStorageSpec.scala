package com.github.oskin1.wallet.persistence

import cats.implicits._
import org.scalatest.Matchers
import org.scalatest.prop.PropertyChecks
import org.scalatest.propspec.AnyPropSpec
import zio.interop.catz._

class LDBStorageSpec
  extends AnyPropSpec
    with Matchers
    with PropertyChecks
    with StorageSpec {

  property("put/get/delete") {
    withRealDb { db =>
      val valueA = (toBytes("A"), toBytes("1"))
      val valueB = (toBytes("B"), toBytes("2"))
      val values = List(valueA, valueB)

      db.put(values).unsafeRunSync()

      values.map(_._1).map(db.get).sequence.unsafeRunSync()
        .map(_.map(toString)) should contain theSameElementsAs values.map(x => Some(toString(x._2)))

      db.delete(valueA._1).unsafeRunSync()

      db.get(valueA._1).unsafeRunSync() shouldBe None
    }
  }

}
