# Cleia Backend (multi-modules)
- Parent: fr.cleia:cleia-backend:0.1.0
- Java 25, Spring Boot 3.5.6
- Modules: shared-kernel, domain, application, infrastructure, web

## Build
./mvnw -q -ntp clean install

## Run (module web)
./mvnw -q -ntp -pl web spring-boot:run
