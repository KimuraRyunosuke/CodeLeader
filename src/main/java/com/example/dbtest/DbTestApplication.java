package com.example.dbtest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootApplication
public class DbTestApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DbTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String db = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?sslmode=require", host, port, db);

        System.out.println("JDBC URL: " + jdbcUrl);
        System.out.println("Trying to connect...");

        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password)) {
            System.out.println("Connection SUCCESS!");
        } catch (Exception e) {
            System.err.println("Connection FAILED!");
            e.printStackTrace();
        }
    }
}
