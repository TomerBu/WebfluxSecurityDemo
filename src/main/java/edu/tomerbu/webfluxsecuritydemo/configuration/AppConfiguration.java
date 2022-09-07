package edu.tomerbu.webfluxsecuritydemo.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "edu.tomerbu")
@Data
public class AppConfiguration {
    public String jwtSecret = "SECRET_KEY";
    public long jwtExpiration = 86400000;
}