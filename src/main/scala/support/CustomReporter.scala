package support

import org.scalatest.Reporter
import org.scalatest.events._

// see http://doc.scalatest.org/3.0.0/index.html#org.scalatest.Reporter
class CustomReporter(reporter: Reporter) extends Reporter {
  def apply(event: Event): Unit = {
    event match {
      case e: TestFailed =>
        val message = e.throwable match {
          case Some(err: MyTestPendingException) => Formatter.formatError(err, e.suiteName, e.testName)
          case Some(err: MyNotImplementedException) => Formatter.formatError(err, e.suiteName, e.testName)
          case Some(err: MyTestFailedException) => Formatter.formatError(err, e.suiteName, e.testName)
          case Some(err: MyException) => Formatter.formatError(err, e.suiteName, e.testName)
          case _ => "unexpected error : " + e
        }
        println("\n" + message + "\n")
        event.ordinal.nextNewOldPair._2.next
        CustomStopper.requestStop()
      case e: TestStarting => //println("CustomReporter.apply(TestStarting, " + e.testName + ")")
      case e: TestSucceeded => //println("CustomReporter.apply(TestSucceeded, " + e.testName + ")")
      case e: TestIgnored => //println("CustomReporter.apply(TestIgnored, " + e.testName + ")")
      case e: ScopeOpened => //println("CustomReporter.apply(ScopeOpened, " + e.message + ")")
      case e: ScopeClosed => //println("CustomReporter.apply(ScopeClosed, " + e.message + ")")
      case e: InfoProvided => //println("CustomReporter.apply(InfoProvided, " + e.message + ")")
      // useless
      case e: DiscoveryStarting => println("CustomReporter.apply(DiscoveryStarting) => " + e)
      case e: DiscoveryCompleted => println("CustomReporter.apply(DiscoveryCompleted) => " + e)
      case e: RunStarting => println("CustomReporter.apply(RunStarting) => " + e)
      case e: RunStopped => println("CustomReporter.apply(RunStopped) => " + e)
      case e: RunAborted => println("CustomReporter.apply(RunAborted) => " + e)
      case e: RunCompleted => println("CustomReporter.apply(RunCompleted) => " + e)
      case e: ScopePending => println("CustomReporter.apply(ScopePending) => " + e)
      case e: TestCanceled => println("CustomReporter.apply(TestCanceled) => " + e)
      case e: TestPending => println("CustomReporter.apply(TestPending) => " + e)
      case e: SuiteStarting => println("CustomReporter.apply(SuiteStarting) => " + e)
      case e: SuiteCompleted => println("CustomReporter.apply(SuiteCompleted) => " + e)
      case e: SuiteAborted => println("CustomReporter.apply(SuiteAborted) => " + e)
      case e: MarkupProvided => println("CustomReporter.apply(MarkupProvided) => " + e)
      case e: AlertProvided => println("CustomReporter.apply(AlertProvided) => " + e)
      case e: NoteProvided => println("CustomReporter.apply(NoteProvided) => " + e)
    }
    // other is a org.scalatest.DispatchReporter which counts execs for the final summary
    // not calling it will ends with an "Empty test suite." unless you do the Summary
    //other(event)
  }
}
