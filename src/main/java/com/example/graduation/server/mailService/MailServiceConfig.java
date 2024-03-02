package com.example.graduation.server.mailService;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:application.yml"},encoding = "UTF-8")
@Data
public class MailServiceConfig {
    @Value("${sms.server.smsValidTimeInterval}")
    private long smsValidTimeInterval;//过期时间
    @Value("${sms.server.length}")
    private int length;
    @Value("${sms.server.title}")
    private String title;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public MailService mailService() {
        return new MailService();
    }
}
