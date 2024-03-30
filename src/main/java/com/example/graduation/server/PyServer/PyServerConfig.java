package com.example.graduation.server.PyServer;

import com.example.graduation.server.mailService.MailService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:application.yml"},encoding = "UTF-8")
@Data
public class PyServerConfig {
    @Value("${pyServer.url}")
    private String url;

    @Bean
    public PyServerConfig PyServerConfig() {
        return new PyServerConfig();
    }
}
