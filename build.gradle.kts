import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "adrianrafo"
version = "1.0-SNAPSHOT"

buildscript {
  var kotlinVersion: String by extra
  kotlinVersion = "1.2.10"

  repositories {
    mavenCentral()
  }

  dependencies {
    classpath(kotlinModule("gradle-plugin", kotlinVersion))
  }

}

apply {
  plugin("kotlin")
}

val kotlinVersion: String by extra
val arrowVersion="0.6.1"

repositories {
  mavenCentral()
  jcenter()

}

dependencies {
  compile(kotlinModule("stdlib-jdk8", kotlinVersion))
  compile("io.arrow-kt:arrow-core:$arrowVersion")
  compile("io.arrow-kt:arrow-typeclasses:$arrowVersion")
  compile("io.arrow-kt:arrow-instances:$arrowVersion")
  compile("io.arrow-kt:arrow-data:$arrowVersion")
  compile("io.arrow-kt:arrow-syntax:$arrowVersion")
  kapt("io.arrow-kt:arrow-annotations-processor:$arrowVersion")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

