package com.odyssey;

import com.odyssey.services.cloudinary.CloudinaryConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CloudinaryConfigProperties.class)
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
