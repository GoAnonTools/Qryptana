plugins {
  `java-library`
}

// Built from NanoHTTPD commit efb2ebf85a2b06f7c508aba9eaad5377e3a01e81.
// Only the core and websocket modules are included; samples are excluded.
group = "org.nanohttpd"
version = "efb2ebf"

val core = layout.projectDirectory.dir("core")
val websocket = layout.projectDirectory.dir("websocket")

sourceSets {
  main {
    java {
      setSrcDirs(listOf(core.dir("src/main/java"), websocket.dir("src/main/java")))
      exclude("org/nanohttpd/samples/**")
    }
    resources.setSrcDirs(listOf(core.dir("src/main/resources")))
  }
}

java {
  val jvmVersion = JavaVersion.toVersion(providers.gradleProperty("kotlin.jvm.target").get())
  sourceCompatibility = jvmVersion
  targetCompatibility = jvmVersion
}

val nanohttpdSources = sourceSets.main.get().java.matching { include("org/nanohttpd/**") }

val checkNanohttpdSources by tasks.registering {
  doFirst {
    if (nanohttpdSources.isEmpty) {
      throw GradleException("NanoHTTPD sources are missing")
    }
  }
}

tasks.compileJava {
  dependsOn(checkNanohttpdSources)
}

tasks.jar {
  isPreserveFileTimestamps = false
  isReproducibleFileOrder = true
  filePermissions { unix("644") }
  dirPermissions { unix("755") }
}
