package fr.cleia.sia.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication(scanBasePackages = "fr.cleia.sia")
public class CleiaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CleiaBackendApplication.class, args);
    }
}