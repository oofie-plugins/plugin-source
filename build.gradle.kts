//this works

import ProjectVersions.openosrsVersion

buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    java //this enables annotationProcessor and implementation in dependencies
    checkstyle
}

project.extra["GithubUrl"] = "https://github.com/oofie-plugins/plugins"

apply<BootstrapPlugin>()

allprojects {
    group = "com.openosrs.externals"
    apply<MavenPublishPlugin>()
}

allprojects {
    apply<MavenPublishPlugin>()

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}

subprojects {
    group = "com.openosrs.externals"

    project.extra["PluginProvider"] = "Birthday Cat#7226"
    project.extra["ProjectSupportUrl"] = "https://discord.gg/m5f7bEv4cm"
    project.extra["PluginLicense"] = "3-Clause BSD License"

    repositories {
        jcenter {
            content {
                excludeGroupByRegex("com\\.openosrs.*")
            }
        }

        exclusiveContent {
            forRepository {
                mavenLocal()
            }
            filter {
                includeGroupByRegex("com\\.openosrs.*")
                includeGroupByRegex("com\\.owain.*")
            }
        }
    }

    apply<JavaPlugin>()

    dependencies {
        annotationProcessor(Libraries.lombok)
        annotationProcessor(Libraries.pf4j)

        compileOnly("com.openosrs:http-api:$openosrsVersion+")
        compileOnly("com.openosrs:runelite-api:$openosrsVersion+")
        compileOnly("com.openosrs:runelite-client:$openosrsVersion+")
        compileOnly("com.openosrs.rs:runescape-api:$openosrsVersion+")

        implementation(group = "com.google.api-client", name = "google-api-client", version = "1.31.3")
        implementation(group = "com.google.api-client", name = "google-api-client-jackson2", version = "1.31.1")

        implementation(group = "com.google.oauth-client", name = "google-oauth-client-jetty", version = "1.31.4")
        implementation(group = "com.google.apis", name = "google-api-services-sheets", version = "v4-rev612-1.25.0")
        implementation(group = "com.google.http-client", name = "google-http-client", version = "1.39.0")
        implementation(group = "com.google.http-client", name = "google-http-client-gson", version = "1.39.0")

        implementation(group = "io.opencensus", name = "opencensus-api", version = "0.28.3")
        implementation(group = "io.opencensus", name = "opencensus-contrib-http-util", version = "0.28.3")
        implementation(group = "io.grpc", name = "grpc-context", version = "1.36.0")

        implementation(group = "com.google.cloud", name = "libraries-bom", version = "19.2.1")



        compileOnly(Libraries.findbugs)
        compileOnly(Libraries.apacheCommonsText)
        compileOnly(Libraries.gson)
        compileOnly(Libraries.guice)
        compileOnly(Libraries.lombok)
        compileOnly(Libraries.okhttp3)
        compileOnly(Libraries.pf4j)
        compileOnly(Libraries.rxjava)
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                url = uri("$buildDir/repo")
            }
        }
        publications {
            register("mavenJava", MavenPublication::class) {
                from(components["java"])
            }
        }
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        register<Copy>("copyDeps") {
            into("./build/deps/")
            from(configurations["runtimeClasspath"])
        }

        withType<Jar> {
            doLast {
                copy {
                    from("./build/libs/")
                    into(System.getProperty("user.home") + "/Documents/JavaProjects/My Plugins Jars")
                }
            }
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            dirMode = 493
            fileMode = 420
        }
    }
}