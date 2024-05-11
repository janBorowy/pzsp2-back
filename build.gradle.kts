plugins {
    id("java")
    id("io.freefair.lombok") version "8.6"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.spockframework:spock-core:2.4-M4-groovy-4.0")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.5")
}

tasks.named<Test>("test"){
    useJUnitPlatform()
}
