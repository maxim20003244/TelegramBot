package org.example.impl;

import org.example.dao.RawDao;
import org.example.entity.RawData;
import org.example.service.MainService;
import org.example.service.ProducerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
@Service
public class MainServiceImpl implements MainService {
   private final ProducerService producerService;
    private  final RawDao rawDao;

    public MainServiceImpl( ProducerService producerService, RawDao rawDao) {
        this.producerService = producerService;
        this.rawDao = rawDao;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawDate(update);

        var message  = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello from Node");
        producerService.producerAnswer(sendMessage);

    }

    private void saveRawDate(Update update) {
        RawData rawData =  RawData.builder()
                .event(update)
                .build();
        rawDao.save(rawData);
    }
}
