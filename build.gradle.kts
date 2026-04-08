plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.br"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Web
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Cache
	implementation("org.springframework.boot:spring-boot-starter-cache")

	// Caffeine — implementação de cache local
	implementation("com.github.ben-manes.caffeine:caffeine")

	// Feign Client — para chamar a API do ViaCEP
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// Testes
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Necessário para o OpenFeign funcionar com Spring Boot
dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}