package org.example.controller;

import lombok.extern.log4j.Log4j;
import org.example.service.impl.UpdateProducerImpl;
import org.example.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.example.RabbitQueue.*;

@Component
@Log4j
public class UpdateProcessor {
    private TelegramBot telegramBot;
    private MessageUtils messageUtils;
    private final UpdateProducerImpl updateProducer;

    public UpdateProcessor(MessageUtils messageUtils, UpdateProducerImpl updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }



    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update){
        if(update ==null){
            log.error("Received update is null");
        }

        if(update.hasMessage()){
            distributeMessageByType(update);
        } else{
            log.error("Unsupported message type is Received: " + update);
        }
    }

    private void distributeMessageByType(Update update) {
   var message = update.getMessage();

       if(message.hasText()){
           processTextMessage(update);
       } else if (message.hasDocument()) {
      processDocumentMessage(update);
       } else if (message.hasPhoto()) {
           processPhotoMessage(update);

       } else {
           setUnsupportedMessageView(update);
       }

    }

    public void setUnsupportedMessageView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Unsupported type message");
        setView(sendMessage);
    }
    public void sendFileReceivedView(Update update){
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "I get the file!Wait...");
        setView(sendMessage);

    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE ,update);
    }

    private void processDocumentMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE ,update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE ,update);
    }
}
