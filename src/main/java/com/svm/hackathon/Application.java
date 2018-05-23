package com.svm.hackathon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion;
import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 
 * Fill in the AWS fields with your values
 * 
 * Root class of the application with main method.
 *
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableRdsInstance(databaseName="ebdb", dbInstanceIdentifier="aa1v4stejtf7drf", username="root",password="secret")
@EnableContextCredentials(accessKey="secret", secretKey="secret")
@EnableContextRegion(region="us-west-1")
public class Application extends SpringBootServletInitializer {
 
    public static void main(String[] args) 
    {
        SpringApplication.run(Application.class, args);
    }
 
}
