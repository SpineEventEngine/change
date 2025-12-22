/*
 * Copyright 2025, TeamDev. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

@file:Suppress("RemoveRedundantQualifierName")

import com.google.protobuf.gradle.id
import io.spine.dependency.build.Dokka
import io.spine.dependency.build.ErrorProne
import io.spine.dependency.kotlinx.Coroutines
import io.spine.dependency.lib.Grpc
import io.spine.dependency.lib.Jackson
import io.spine.dependency.lib.Kotlin
import io.spine.dependency.lib.KotlinPoet
import io.spine.dependency.local.Base
import io.spine.dependency.local.CoreJvm
import io.spine.dependency.local.Logging
import io.spine.dependency.local.ProtoData
import io.spine.dependency.local.ToolBase
import io.spine.dependency.local.Validation
import io.spine.gradle.checkstyle.CheckStyleConfig
import io.spine.gradle.github.pages.updateGitHubPages
import io.spine.gradle.javac.configureErrorProne
import io.spine.gradle.javac.configureJavac
import io.spine.gradle.javadoc.JavadocConfig
import io.spine.gradle.kotlin.setFreeCompilerArgs
import io.spine.gradle.publish.PublishingRepos
import io.spine.gradle.publish.PublishingRepos.gitHub
import io.spine.gradle.publish.spinePublishing
import io.spine.gradle.repo.standardToSpineSdk
import io.spine.gradle.report.coverage.JacocoConfig
import io.spine.gradle.report.license.LicenseReporter
import io.spine.gradle.report.pom.PomGenerator
import io.spine.gradle.testing.configureLogging
import io.spine.gradle.testing.registerTestTasks

buildscript {
    standardSpineSdkRepositories()
    doForceVersions(configurations)

    dependencies {
        classpath(enforcedPlatform(io.spine.dependency.kotlinx.Coroutines.bom))
        classpath(io.spine.dependency.local.Compiler.pluginLib)
        classpath(io.spine.dependency.local.CoreJvmCompiler.pluginLib)
    }

    val validation = io.spine.dependency.local.Validation
    val logging = io.spine.dependency.local.Logging
    val base = io.spine.dependency.local.Base
    configurations {
        all {
            resolutionStrategy {
                val jackson = io.spine.dependency.lib.Jackson
                val cfg = this@all
                val rs = this@resolutionStrategy
                jackson.forceArtifacts(project, cfg, rs)
                io.spine.dependency.lib.Jackson.DataType.forceArtifacts(project, cfg, rs)

                io.spine.dependency.lib.Grpc.forceArtifacts(project, cfg, rs)

                force(
                    io.spine.dependency.lib.Kotlin.bom,
                    io.spine.dependency.lib.Grpc.bom,
                    jackson.annotations,
                    base.annotations,
                    base.lib,
                    validation.runtime,
                    validation.oldRuntime,
                    logging.lib,
                    io.spine.dependency.local.Time.lib,
                    io.spine.dependency.local.Time.javaExtensions,
                )
            }
        }
    }
}

repositories {
    // Required to grab the dependencies for `JacocoConfig`.
    standardToSpineSdk()
}

plugins {
    `java-library`
    kotlin("jvm")
    protobuf
    jacoco
    `project-report`
    errorprone
}

spinePublishing {
    modules = setOf(
        "change",
    )
    destinations = with(PublishingRepos) {
        setOf(
            gitHub("change"),
            cloudArtifactRegistry
        )
    }
}

// Temporarily use this version, since 3.21.x is known to provide
// a broken `protoc-gen-js` artifact and Kotlin code without access modifiers.
// See https://github.com/protocolbuffers/protobuf-javascript/issues/127.
//     https://github.com/protocolbuffers/protobuf/issues/10593
val protocArtifact = "com.google.protobuf:protoc:3.19.6"

allprojects {
    apply(from = "$rootDir/version.gradle.kts")

    group = "io.spine"
    version = extra["versionToPublish"]!!

    configurations {
        forceVersions()
        all {
            exclude("io.spine:spine-validate")
            resolutionStrategy {
                val cfg = this@all
                val rs = this@resolutionStrategy
                Kotlin.StdLib.forceArtifacts(project, cfg, rs)
                Kotlin.forceArtifacts(project, cfg, rs)
                Coroutines.forceArtifacts(project, cfg, rs)
                Jackson.forceArtifacts(project, this@all, this@resolutionStrategy)
                Jackson.DataType.forceArtifacts(project, this@all, this@resolutionStrategy)
                Jackson.DataFormat.forceArtifacts(project, this@all, this@resolutionStrategy)
                Grpc.forceArtifacts(project, this@all, this@resolutionStrategy)
                force(
                    Jackson.annotations,
                    Jackson.bom,
                    Grpc.bom,
                    Kotlin.bom,
                    KotlinPoet.lib,
                    ToolBase.lib,
                    Base.lib,
                    Base.annotations,
                    Validation.runtime,
                    Validation.oldRuntime,
                    Validation.javaBundle,
                    Logging.lib,
                    CoreJvm.server,
                    protocArtifact
                )
            }
        }
    }
}

subprojects {

    apply {
        plugin("java-library")
        plugin("kotlin")
        plugin("com.google.protobuf")
        plugin("net.ltgt.errorprone")
        plugin("pmd")
        plugin("checkstyle")
        plugin("idea")
        plugin("pmd-settings")
        plugin("jacoco")
        plugin("module-testing")
        plugin("dokka-setup")
    }

    repositories {
        gitHub("change")
        standardToSpineSdk()
    }

    dependencies {
        errorprone(ErrorProne.core)
    }

    val javaVersion = JavaVersion.VERSION_17

    java {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion

        tasks {
            withType<JavaCompile>().configureEach {
                configureJavac()
                configureErrorProne()
            }
            withType<org.gradle.jvm.tasks.Jar>().configureEach {
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
        }
    }

    kotlin {
        explicitApi()
        compilerOptions {
            jvmTarget.set(BuildSettings.jvmTarget)
            setFreeCompilerArgs()
        }
    }

    LicenseReporter.generateReportIn(project)
    JavadocConfig.applyTo(project)
    CheckStyleConfig.applyTo(project)

    protobuf {
        protoc {
            // Temporarily use this version, since 3.21.x is known to provide
            // a broken `protoc-gen-js` artifact.
            // See https://github.com/protocolbuffers/protobuf-javascript/issues/127.
            //
            // Once it is addressed, this artifact should be `Protobuf.compiler`.
            artifact = protocArtifact
        }
        generateProtoTasks {
            all().forEach { task ->
                task.builtins {
                    id("js") {
                        option("library=spine-change-${project.version}")
                    }
                }
            }
        }
    }

    updateGitHubPages() {
        rootFolder.set(rootDir)
    }

    project.configureTaskDependencies()
}

LicenseReporter.mergeAllReports(project)
JacocoConfig.applyTo(project)
PomGenerator.applyTo(project)
