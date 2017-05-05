package support

object Formatter {
  private val textWidth = 60
  private val isAssertFailRegex = "(?s)(.*) was not equal to (.*)".r
  val missingValue = "Remplace __ par la valeur attendue"
  val missingImplementation = "Remplace ??? par une implémentation correcte"

  def formatError(e: MyTestPendingException, suiteName: String, testName: String): String = List(
    Some(e.ctx.tmp),
    Some(Section.header(testName, "*")),
    e.message.map(Section.message),
    e.errors.map(Section.code(e.ctx, _)),
    Some(Section.footer(e.fileName, "*"))
  ).flatten.mkString("\n\n")

  def formatError(e: MyNotImplementedException, suiteName: String, testName: String): String = List(
    Some(e.ctx.tmp),
    Some(Section.header(testName, "*")),
    e.message.map(Section.message),
    e.errors.map(Section.code(e.ctx, _)),
    Some(Section.footer(e.fileName, "*"))
  ).flatten.mkString("\n\n")

  def formatError(e: MyTestFailedException, suiteName: String, testName: String): String = List(
    Some(e.ctx.tmp),
    Some(Section.header(testName, "!")),
    e.message.map(Section.message),
    e.errors.map(Section.code(e.ctx, _)),
    Some(Section.footer(e.fileName, "!"))
  ).flatten.mkString("\n\n")

  def formatError(e: MyException, suiteName: String, testName: String): String = List(
    Some(e.ctx.tmp),
    Some(Section.header(testName, "!")),
    e.message.map(Section.message),
    e.errors.map(Section.code(e.ctx, _)),
    Some(Section.stacktrace(e.getCause)),
    Some(Section.footer(e.fileName, "!"))
  ).flatten.mkString("\n\n")

  object Section {
    def header(title: String, char: String): String =
      List(char * textWidth, Utils.padCenter(title, textWidth), char * textWidth).mkString("\n")

    def message(text: String): String = text match {
      case isAssertFailRegex(result, expected) => "Résultat incorrect :("
      case _ => text
    }

    def stacktrace(t: Throwable): String =
      t.getStackTrace.take(7).mkString("\n")

    def code(ctx: TestContext, errors: List[Int]): String =
      Code.format(ctx, errors)

    def footer(fileName: Option[String], char: String): String =
      List(Some(char * textWidth), fileName).flatten.mkString("\n")
  }

  object Code {
    def formatLine(lineNumber: Int, line: String, hasError: Boolean): String =
      (if (hasError) " ->" else "   ") + Utils.padLeft(lineNumber.toString, 4) + " |" + line

    def formatBlock(lines: List[(Int, String)], error: Int): List[String] =
      lines.map { case (lineNumber, codeLine) => formatLine(lineNumber, codeLine, lineNumber == error) }

    def formatError(ctx: TestContext, error: Int): String =
      formatBlock(ctx.lines.slice(error - 2, error + 1), error).mkString("\n")

    def formatTest(ctx: TestContext, error: Int): String =
      formatBlock(ctx.lines.slice(ctx.startLine - 1, ctx.endLine + 1), error).mkString("\n")

    def format(ctx: TestContext, errors: List[Int]): String = {
      val (inTest, outTest) = errors.partition(line => ctx.startLine <= line && line <= ctx.endLine)
      val formattedTest = if (inTest.nonEmpty) formatTest(ctx, inTest.min) else ""
      val formattedErrors = outTest.sorted.map(i => formatError(ctx, i)).mkString("\n")
      val split = if (formattedErrors.isEmpty) "" else "\n          ..."
      formattedErrors + split + formattedTest
    }
  }

  object Utils {
    def padLeft(text: String, size: Int, char: String = " "): String =
      (char * (size - text.length)) + text

    def padRight(text: String, size: Int, char: String = " "): String =
      text + (char * (size - text.length))

    def padCenter(text: String, size: Int, char: String = " "): String =
      (char * ((size - text.length) / 2)) + text
  }

}
