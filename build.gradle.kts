plugins {
//    kotlin("multiplatform")
    java
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    testImplementation("junit:junit:${project.properties["junit_version"]}")//${extra["junit_version"]}
}

group = "org.quinto.dawg"
version = "1.0"
//sourceCompatibility = "1.7"

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
/*
kotlin {
    jvm() {
//        withJava()
    }
    js(IR) {
        browser()
    }
    sourceSets {
        named("commonMain") {
            kotlin.srcDirs("src/main/java")
        }
    }
}
 */