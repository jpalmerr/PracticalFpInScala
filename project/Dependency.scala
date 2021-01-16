import sbt._
object Dependency {

  private object Version {
    val Cats = "2.1.0"
    val Profunktor = "0.8.1"
    val Manatki = "0.10.5"
    val Fs2 = "2.2.2"
    val Meow = "0.4.0"
    val NewType = "0.4.3"
    val Refined = "0.9.12"
    val Monocle = "2.0.1"
  }

  val deps: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core" % Version.Cats,
    "org.typelevel" %% "cats-effect" % Version.Cats,
    "dev.profunktor" %% "console4cats" % Version.Profunktor,
    "org.manatki" %% "derevo-cats" % Version.Manatki,
    "org.manatki" %% "derevo-cats-tagless" % Version.Manatki,
    "co.fs2" %% "fs2-core" % Version.Fs2,
    "com.olegpy" %% "meow-mtl-core" % Version.Meow,
    "com.olegpy" %% "meow-mtl-effects" % Version.Meow,
    "io.estatico" %% "newtype" % Version.NewType,
    "eu.timepit" %% "refined" % Version.Refined,
    "com.github.julien-truffaut" %% "monocle-core" % Version.Monocle,
    "com.github.julien-truffaut" %% "monocle-macro" % Version.Monocle
  )

  val compilerPlugins: Seq[ModuleID] = Seq(
    compilerPlugin(
      "org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full
    ),
    compilerPlugin(
      "org.augustjune" %% "context-applied" % "0.1.2"
    )
  )

  def apply(): Seq[ModuleID]  =  deps ++ compilerPlugins
}
