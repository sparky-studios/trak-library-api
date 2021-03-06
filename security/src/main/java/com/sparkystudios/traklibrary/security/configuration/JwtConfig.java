package com.sparkystudios.traklibrary.security.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtConfig {

    @Value("${trak.security.jwt.secret-key}")
    private String secretKey;

    @Value("${trak.security.jwt.expiry-time}")
    private long expiryTime;
}
