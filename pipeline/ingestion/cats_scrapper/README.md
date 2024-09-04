## sbt project compiled with Scala 3

## TODO
> Move println exceptions in Scrapper to logger library that saves run logs to a dated file
> Return a pure list of titles from Scrapper.fetchProducts
> Scheduler repeating Scrapper every 7 minutes and returning to Manager
> Oracle titles to PSQL database and run Scheduler which runs Scrapper 

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).
