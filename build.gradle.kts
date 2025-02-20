plugins {
  kotlin("jvm") version "2.1.10"
  id("com.diffplug.spotless") version "6.25.0"
}

group = "uk.ac.york.gpig.teamb"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
  testImplementation(kotlin("test"))
  testImplementation("io.strikt:strikt-core:0.35.1")
  testImplementation("io.kotest:kotest-runner-junit5-jvm:6.0.0.M2")
  testImplementation("io.kotest:kotest-property:6.0.0.M2") // property-based testing
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(21) }

spotless {
  kotlin {
    ktfmt()
    ktlint()
    target("src/**/*.kt")
    toggleOffOn()
  }
  kotlinGradle {
    target("*.gradle.kts") // default target for kotlinGradle
    ktfmt()
  }
}
