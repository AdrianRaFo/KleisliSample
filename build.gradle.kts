import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "adrianrafo"
version = "1.0-SNAPSHOT"

buildscript {
  var kotlin_version: String by extra
  kotlin_version = "1.2.10"

  repositories {
    mavenCentral()
  }

  dependencies {
    classpath(kotlinModule("gradle-plugin", kotlin_version))
  }

}

apply {
  plugin("kotlin")
}

val kotlin_version: String by extra

repositories {
  mavenCentral()
  jcenter()

}

dependencies {
  compile(kotlinModule("stdlib-jdk8", kotlin_version))
  compile("io.arrow-kt:arrow-core:0.6.1")
  compile("io.arrow-kt:arrow-typeclasses:0.6.1")
  compile("io.arrow-kt:arrow-instances:0.6.1")
  compile("io.arrow-kt:arrow-data:0.6.1")
  compile("io.arrow-kt:arrow-syntax:0.6.1")
  kapt("io.arrow-kt:arrow-annotations-processor:0.6.1")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

