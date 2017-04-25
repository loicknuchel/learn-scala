package support

import org.scalatest.{FunSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.Matcher

trait WorkshopSuite extends FunSpec with Matchers with ScalaFutures {
  def __ : Matcher[Any] = ???
  val exercice = it
}
