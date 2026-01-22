package com.fpt.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class AssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplication.class, args);
        System.out.println("Running...");
    }
    
}