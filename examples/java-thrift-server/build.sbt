import sbt.Keys._

parallelExecution in ThisBuild := false

lazy val versions = new {
  val finatra = "2.11.0"
  val guice = "4.0"
  val logback = "1.1.7"
  val mockito = "1.9.5"
  val junit = "4.12"
  val scalacheck = "1.13.4"
  val scalatest = "3.0.0"
  val specs2 = "2.4.17"
}

lazy val baseSettings = Seq(
  version := "2.11.0",
  scalaVersion := "2.12.1",
  ivyScala := ivyScala.value.map(_.copy(overrideScalaVersion = true)),
  libraryDependencies ++= Seq(
    "org.mockito" % "mockito-core" % versions.mockito % "test",
    "org.scalacheck" %% "scalacheck" % versions.scalacheck % "test",
    "org.scalatest" %% "scalatest" %  versions.scalatest  % "test",
    "org.specs2" %% "specs2-mock" % versions.specs2 % "test",
    "org.mockito" % "mockito-core" % versions.mockito % "test",
    "junit" % "junit" % versions.junit % "test"
  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases")
  ),
  fork in run := true,
  assemblyMergeStrategy in assembly := {
    case "BUILD" => MergeStrategy.discard
    case "META-INF/io.netty.versions.properties" => MergeStrategy.last
    case other => MergeStrategy.defaultMergeStrategy(other)
  }
)

lazy val root = (project in file(".")).
  settings(
    name := "java-thrift-server",
    organization := "com.twitter",
    moduleName := "thrift-example-root",
    run := {
      (run in `thriftExampleServer` in Compile).evaluated
    }
  ).
  aggregate(thriftExampleServer)

lazy val thriftExampleServer = (project in file("thrift-example-server")).
  settings(baseSettings).
  settings(
    name := "thrift-example-server",
    moduleName := "thrift-example-server",
    mainClass in (Compile, run) := Some("com.twitter.calculator.CalculatorServerMain"),
    libraryDependencies ++= Seq(
      "com.twitter" %% "finatra-thrift" % versions.finatra,
      "ch.qos.logback" % "logback-classic" % versions.logback,

      "com.twitter" %% "finatra-thrift" % versions.finatra % "test",
      "com.twitter" %% "inject-app" % versions.finatra % "test",
      "com.twitter" %% "inject-core" % versions.finatra % "test",
      "com.twitter" %% "inject-modules" % versions.finatra % "test",
      "com.twitter" %% "inject-server" % versions.finatra % "test",
      "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",

      "com.twitter" %% "finatra-thrift" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests"
    )
  ).
  dependsOn(thriftExampleIdl)

lazy val thriftExampleIdl = (project in file("thrift-example-idl")).
  settings(baseSettings).
  settings(
    name := "thrift-example-idl",
    moduleName := "thrift-example-idl",
    scroogeLanguages in Compile := Seq("java"),
    scroogeThriftDependencies in Compile := Seq(
      "finatra-thrift_2.11"
    ),
    libraryDependencies ++= Seq(
      "com.twitter" %% "finatra-thrift" % versions.finatra
    )
  )
