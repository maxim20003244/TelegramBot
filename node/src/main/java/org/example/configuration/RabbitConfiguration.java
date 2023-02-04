package org.example.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.example.RabbitQueue.*;
import static org.example.RabbitQueue.ANSWER_MESSAGE;
@Configuration
public class RabbitConfiguration {
    @Bean
    public MessageConverter jsonMessageConverted( ){
        return  new Jackson2JsonMessageConverter();

    }

}
