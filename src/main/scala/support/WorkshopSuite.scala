package support

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.exceptions.{TestFailedException, TestPendingException}
import org.scalatest.matchers.Matcher
import org.scalatest.time.{Millis, Seconds, Span}

import scala.concurrent.duration._
import scala.concurrent.{Await, Awaitable}
import scala.language.experimental.macros
import scala.language.postfixOps

trait WorkshopSuite extends FunSpec with Matchers with ScalaFutures {
  implicit val suite: WorkshopSuite = this
  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  def await[T](awaitable: Awaitable[T]): T = Await.result(awaitable, 1 second)

  def __ : Matcher[Any] = throw new TestPendingException

  def section = describe _

  def exercice(testName: String)(testFun: Unit)(implicit suite: WorkshopSuite): Unit = macro ExerciceMacro.apply

  def exerciceImpl(testName: String)(testFun: => Unit)(ctx: TestContext): Unit = {
    it(testName) {
      try {
        testFun
      } catch {
        case e: TestPendingException => throw CustomExceptions.pending(suite, ctx, e)
        case e: NotImplementedError => throw CustomExceptions.notImplemented(suite, ctx, e)
        case e: TestFailedException => throw CustomExceptions.failed(suite, ctx, e)
        case e: Throwable => throw CustomExceptions.unknown(suite, ctx, e)
      }
    }
  }

  // see http://doc.scalatest.org/3.0.0/index.html#org.scalatest.FunSpecLike@run(testName:Option[String],args:org.scalatest.Args):org.scalatest.Status
  override def run(testName: Option[String], args: Args): Status = {
    super.run(testName, args.copy(reporter = new CustomReporter(args.reporter), stopper = CustomStopper))
  }
}
