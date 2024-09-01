package scrapper.demo_web_scrapper


final class demo_scrapper$u002Eworksheet$_ {
def args = demo_scrapper$u002Eworksheet_sc.args$
def scriptPath = """scrapper/demo_web_scrapper/demo_scrapper.worksheet.sc"""
/*<script>*/

/*</script>*/ /*<generated>*//*</generated>*/
}

object demo_scrapper$u002Eworksheet_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new demo_scrapper$u002Eworksheet$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export demo_scrapper$u002Eworksheet_sc.script as `demo_scrapper.worksheet`

