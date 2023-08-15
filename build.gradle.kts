import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "es.niadecode"
version = "1.0-SNAPSHOT"
val ktor_version: String by project


repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven ("https://jitpack.io")
}

kotlin {
    jvm {
        jvmToolchain(11)
     //   withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                api(compose.runtime)
                api(compose.foundation)
                api(compose.animation)
                api(compose.material)
                api(compose.preview)

                val precomposeVersion = "1.4.3"
                api("moe.tlaster:precompose:$precomposeVersion")
                api("moe.tlaster:precompose-viewmodel:$precomposeVersion")

                api("com.github.Gikkman:Java-Twirk:0.7.1")

                implementation("io.ktor:ktor-server-core:2.3.3")
                implementation("io.ktor:ktor-server-netty:2.3.3")

                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-cio:$ktor_version")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "NumericaBattleRoyale"
            packageVersion = "1.0.0"
        }
    }
}
