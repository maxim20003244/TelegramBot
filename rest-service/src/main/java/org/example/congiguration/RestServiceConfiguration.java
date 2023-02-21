package org.example.congiguration;

import org.example.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
public class RestServiceConfiguration  {
    @Value("${salt}")
    private String salt;

    @Bean
    public CryptoTool getCryptoToll(){
        return new CryptoTool(salt);
    }
}
