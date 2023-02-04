package org.example.configurarion;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.example.RabbitQueue.*;

@Configuration
public class RabbitConfiguration {
   @Bean
    public MessageConverter jsonMessageConverted( ){
        return  new Jackson2JsonMessageConverter();

    }
    @Bean
    public Queue textMessageQueue(){
        return new Queue(TEXT_MESSAGE_UPDATE);

    }   @Bean
    public Queue phototMessageQueue(){
        return new Queue(PHOTO_MESSAGE_UPDATE);

    }   @Bean
    public Queue documentMessageQueue(){
        return new Queue(DOC_MESSAGE_UPDATE);

    }   @Bean
    public Queue answertMessageQueue(){
        return new Queue(ANSWER_MESSAGE);

    }
}
