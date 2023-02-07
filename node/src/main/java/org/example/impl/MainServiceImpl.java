package org.example.impl;

import org.example.dao.AppUserDAO;
import org.example.dao.RawDao;
import org.example.entity.AppUser;
import org.example.entity.RawData;

import org.example.service.MainService;
import org.example.service.ProducerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.example.entity.enums.UserState.BASIC_STATE;

@Service
public class MainServiceImpl implements MainService {
   private final ProducerService producerService;
    private  final RawDao rawDao;
    private final AppUserDAO appUserDAO;

    public MainServiceImpl(ProducerService producerService, RawDao rawDao, AppUserDAO appUserDAO) {
        this.producerService = producerService;
        this.rawDao = rawDao;
        this.appUserDAO = appUserDAO;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawDate(update);

        var textMessage = update.getMessage();
        var telegramUser = textMessage.getFrom();
        var appUser = findOrSaveAppUser(telegramUser);

        var message  = update.getMessage();
        var sendMessage = new SendMessage();

        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello from Node");
        producerService.producerAnswer(sendMessage);

    }

        private AppUser findOrSaveAppUser(User telegramUser){
        AppUser persistenAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if(persistenAppUser == null){
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO change value as default after added users;
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
  return persistenAppUser;
        }

    private void saveRawDate(Update update) {
        RawData rawData =  RawData.builder()
                .event(update)
                .build();
        rawDao.save(rawData);
    }
}
