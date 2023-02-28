package org.example.service.impl;

import org.example.controller.UpdateProcessor;
import org.example.service.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.example.RabbitQueue.ANSWER_MESSAGE;

@Service
public class AnswerConsumerIml implements AnswerConsumer {
    private final UpdateProcessor updateProcessor;

    public AnswerConsumerIml(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateProcessor.setView(sendMessage);

    }
}
