plugins {
    id("java")
    id ("org.springframework.boot") version "3.5.0"
    id ("io.spring.dependency-management") version "1.1.4"
    id("maven-publish")
}

group = "org.project"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-aop:3.5.0")
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.5.0")
    implementation("org.springframework.kafka:spring-kafka:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.5.0")
    // // Hibernate
    // implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    // implementation("org.hibernate.orm:hibernate-community-dialects:6.4.4.Final")
    // implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    // implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.5.0")

    // //Flyway
    // implementation("org.flywaydb:flyway-core:10.14.0")
    // implementation("org.flywaydb:flyway-database-postgresql:10.14.0")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.0")
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("com.h2database:h2:2.2.224")
    runtimeOnly("org.postgresql:postgresql:42.7.3")
}
tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}
tasks.getByName<Jar>("jar") {
    enabled = true
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "org.project"
            artifactId = "human-simulation-starter"
            version = "1.0-SNAPSHOT"
        }
    }
}
tasks.test {
    useJUnitPlatform()
}