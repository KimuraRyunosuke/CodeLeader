package com.example.codeleader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class EnvLogger implements ApplicationRunner {
    private final Logger log = LoggerFactory.getLogger(EnvLogger.class);

    @Override
    public void run(ApplicationArguments args) {
        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        log.info("DEBUG: DB_HOST='{}' DB_PORT='{}' (remove EnvLogger after debugging)", host, port);
    }
}
