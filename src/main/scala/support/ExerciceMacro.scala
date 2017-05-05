package support

import scala.reflect.macros.blackbox.Context

class ExerciceMacro[C <: Context](val c: C) {
  import c.universe._

  def apply(testName: c.Expr[String])(testFun: c.Expr[Unit])(suite: c.Expr[WorkshopSuite]): c.Expr[Unit] = {
    val code = testFun.tree.pos.source.content.mkString
    val sb = new StringBuilder()
    sb.append("tree: "+testFun.tree+"\n")
    val (start, end) = testFun.tree match {
      case Block(xs, expr) =>
        sb.append("Block() => ("+testFun.tree.pos.line+", "+expr.pos.line+")\n")
        (testFun.tree.pos.line, expr.pos.line)
      case _ =>
        sb.append("other => ("+testFun.tree.pos.line+", "+testFun.tree.pos.line+")\n")
        (testFun.tree.pos.line, testFun.tree.pos.line)
    }
    c.Expr(q"""$suite.exerciceImpl($testName)($testFun)(new support.TestContext($code, $start, $end, ${sb.toString}))""")
  }
}

object ExerciceMacro {
  def apply(c: Context)(testName: c.Expr[String])(testFun: c.Expr[Unit])(suite: c.Expr[WorkshopSuite]): c.Expr[Unit] = {
    new ExerciceMacro[c.type](c).apply(testName)(testFun)(suite)
  }
}
