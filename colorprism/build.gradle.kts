import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.dokka)
    alias(libs.plugins.maven.publish)
}

val groupId = "io.github.hyperiontechllc.compose"
val artifactId = "colorprism"
val version = "1.0.0"

android {
    namespace = "dev.hyperiontech.composecolorprism"
    val javaVersion: JavaVersion = JavaVersion.toVersion(libs.versions.java.get())
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        compose = true
    }
}

mavenPublishing {
    coordinates(
        groupId = groupId,
        artifactId = artifactId,
        version = version,
    )

    pom {
        name.set("ColorPrism Compose")
        description.set("A Jetpack Compose color picker library with gesture-driven UI and customizable theming.")
        inceptionYear.set("2025")
        url.set("https://github.com/hyperiontechllc/colorprism")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("ValdrinMaloku")
                name.set("Valdrin Maloku")
                email.set("valimaloku32@gmail.com")
            }
        }
        scm {
            url.set("https://github.com/hyperiontechllc/compose-colorprism")
            connection.set("scm:git:git://github.com/hyperiontechllc/compose-colorprism.git")
            developerConnection.set("scm:git:ssh://git@github.com/hyperiontechllc/compose-colorprism.git")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
}
