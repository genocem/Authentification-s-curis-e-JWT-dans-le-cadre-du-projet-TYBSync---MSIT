package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class Tp {
    public static void main(String[] args) {
        SpringApplication.run(Tp.class, args);
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Connected to MySQL!");
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}