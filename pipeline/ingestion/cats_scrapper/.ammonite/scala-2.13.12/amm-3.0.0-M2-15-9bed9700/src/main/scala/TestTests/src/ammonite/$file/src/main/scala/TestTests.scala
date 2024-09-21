package ammonite
package $file.src.main.scala
import _root_.ammonite.interp.api.InterpBridge.{value => interp}
import _root_.ammonite.interp.api.InterpBridge.value.{exit, scalaVersion}
import _root_.ammonite.interp.api.IvyConstructor.{ArtifactIdExt, GroupIdExt}
import _root_.ammonite.compiler.CompilerExtensions.{
  CompilerInterpAPIExtensions,
  CompilerReplAPIExtensions
}
import _root_.ammonite.runtime.tools.{browse, grep, time, tail}
import _root_.ammonite.compiler.tools.{desugar, source}
import _root_.mainargs.{arg, main}
import _root_.ammonite.repl.tools.Util.{PathRead}
import _root_.ammonite.repl.ReplBridge.value.{codeColorsImplicit}

object TestTests {
  /*<script>*/
  import scraper.Scraper

  /*<amm>*/
  val res_1 = /*</amm>*/ Scraper.scrapeProducts("https://archlinux.org/")

  /*</script>*/ /*<generated>*/
  def $main()           = { _root_.scala.Iterator[String]() }
  override def toString = "TestTests"
  /*</generated>*/
}
