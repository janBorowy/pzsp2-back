plugins {
    id("java")
    id("io.freefair.lombok") version "8.6"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val springVersion = "3.2.5"

dependencies {
    testImplementation("org.spockframework:spock-core:2.4-M4-groovy-4.0")
    implementation("org.springframework.boot:spring-boot-starter-web:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-security:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-test:${springVersion}")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.auth0:java-jwt:4.4.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("com.h2database:h2:2.2.224")
}

tasks.named<Test>("test"){
    useJUnitPlatform()
}
