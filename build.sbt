name := "SpecHelper"

scalaVersion := "2.11.6"

version := "0.01.00"

lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "edu.spectrum"
  )

autoScalaLibrary := true

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  artifact.name + "-" + module.revision + "." + artifact.extension
}

resolvers ++= Seq(
  "Sonatype snapshots"             at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype releases"              at "http://oss.sonatype.org/content/repositories/releases",
  "Java.net Maven2 Repository"     at "http://download.java.net/maven/2/",
  "cglib"     at "http://mvnrepository.com/artifact/cglib/cglib-nodep",
  "apache.repo" at "https://repository.apache.org/content/repositories/snapshots/",
  "sonatype.repo" at "https://oss.sonatype.org/content/repositories/public/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"
)

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "org.apache.commons" % "commons-math3" % "3.3",
  "jfree" % "jcommon" % "1.0.16",
  "jfree" % "jfreechart" % "1.0.13"
)

mainClass in assembly := Some("edu.spectrum.SpecHelper")

addCommandAlias("c", "compile")
addCommandAlias("a", "assembly")


