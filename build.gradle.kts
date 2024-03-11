plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val koin_version = "3.5.0"
    val kotlinVersion = "1.9.0"
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    //KOIN <3
    implementation("io.insert-koin:koin-core:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("io.insert-koin:koin-test-junit5:$koin_version")
    //JACKSON XML
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.15.0")
    //reflections
    implementation(kotlin("reflect"))

}



tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}