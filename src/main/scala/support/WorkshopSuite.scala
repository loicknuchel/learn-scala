package support

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.Matcher

trait WorkshopSuite extends FunSpec with Matchers with ScalaFutures {
  def __ : Matcher[Any] = ???
  def section = describe _
  val exercice = it

  // see http://doc.scalatest.org/3.0.0/index.html#org.scalatest.FunSpecLike@run(testName:Option[String],args:org.scalatest.Args):org.scalatest.Status
  override def run(testName: Option[String], args: Args): Status = {
    super.run(testName, args.copy(reporter = new CustomReporter(args.reporter), stopper = CustomStopper))
  }
}
