package org.example.configuration;

import org.example.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NodeConfiguration {
    @Value("${salt}")
    private String salt;

   @Bean
    public CryptoTool getCryptoToll(){
        return new CryptoTool(salt);
    }
}
