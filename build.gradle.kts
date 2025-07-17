plugins {
    id("java")
    id ("org.springframework.boot") version "3.5.0"
    id ("io.spring.dependency-management") version "1.1.4"
}

group = "org.project"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // // Hibernate
    // implementation("org.hibernate.orm:hibernate-core:6.4.4.Fina")
    // implementation("org.hibernate.orm:hibernate-community-dialects:6.4.4.Final")
    // implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    // implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // //Flyway
    // implementation("org.flywaydb:flyway-core")
    // implementation("org.flywaydb:flyway-database-postgresql")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core")
    testImplementation("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
}

tasks.test {
    useJUnitPlatform()
}