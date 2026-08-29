/*
 * Copyright 2026, TeamDev. All rights reserved.
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

import io.spine.gradle.publish.PublishingRepos
import io.spine.gradle.publish.spinePublishing
import io.spine.gradle.repo.standardToSpineSdk
import io.spine.gradle.report.license.LicenseReporter
import io.spine.gradle.report.pom.PomGenerator

buildscript {
    standardSpineSdkRepositories()

    dependencies {
        classpath(enforcedPlatform(io.spine.dependency.kotlinx.Coroutines.bom))
        classpath(io.spine.dependency.local.Compiler.pluginLib)
        classpath(io.spine.dependency.local.CoreJvmCompiler.gradlePlugin)
    }

    configurations {
        all {
            resolutionStrategy {
                force(
                    io.spine.dependency.lib.Kotlin.bom,
                    // Floor artifacts request the pre-refresh versions of
                    // these; the Protobuf runtime must never be older than
                    // the refreshed gencode.
                    io.spine.dependency.kotlinx.Coroutines.bom,
                    io.spine.dependency.kotlinx.AtomicFu.lib,
                    io.spine.dependency.lib.Protobuf.javaLib,
                    io.spine.dependency.lib.Caffeine.lib,
                    io.spine.dependency.build.Dokka.BasePlugin.lib,
                    io.spine.dependency.local.Base.lib,
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

allprojects {
    apply(from = "$rootDir/version.gradle.kts")
    group = "io.spine"
    version = extra["versionToPublish"]!!

    configurations.all {
        resolutionStrategy {
            // Floor artifacts (the currently published Time and Validation)
            // request the pre-refresh versions of these; the Protobuf runtime
            // must never be older than the refreshed gencode.
            force(
                io.spine.dependency.kotlinx.Coroutines.bom,
                io.spine.dependency.kotlinx.AtomicFu.lib,
                io.spine.dependency.lib.Protobuf.javaLib,
                io.spine.dependency.lib.Caffeine.lib,
            )
        }
    }
}

LicenseReporter.mergeAllReports(project)
PomGenerator.applyTo(project)
