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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.5")
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.5")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.auth0:java-jwt:4.4.0")
}

tasks.named<Test>("test"){
    useJUnitPlatform()
}
