package org.example.impl;

import lombok.extern.log4j.Log4j;
import org.example.dao.AppUserDAO;
import org.example.dao.RawDao;
import org.example.entity.AppDocument;
import org.example.entity.AppUser;
import org.example.entity.RawData;

import org.example.exceptions.UploadFileException;
import org.example.service.FileService;
import org.example.service.MainService;
import org.example.service.ProducerService;
import org.example.service.enums.ServiceCommands;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.example.entity.enums.UserState.BASIC_STATE;
import static org.example.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static org.example.service.enums.ServiceCommands.*;

@Service
@Log4j
public class MainServiceImpl implements MainService {
   private final ProducerService producerService;
    private  final RawDao rawDao;
    private final AppUserDAO appUserDAO;
    private final FileService fileService;

    public MainServiceImpl(ProducerService producerService, RawDao rawDao, AppUserDAO appUserDAO, FileService fileService) {
        this.producerService = producerService;
        this.rawDao = rawDao;
        this.appUserDAO = appUserDAO;
        this.fileService = fileService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawDate(update);
        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var output = "";

  var serviceCommands = ServiceCommands.fromValue(text);
        if(CANCEL.equals(serviceCommands)){
            output = cancelProcess(appUser);
        } else if(BASIC_STATE.equals(userState)){
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
         //TODO add email porcessing

        }else {
            log.error("Unknown user state: " + userState);
            output = "Unknown error ! Enter /cancel and try again!";
        }

        var chatId = update.getMessage().getChatId();
        sendAnswer (output, chatId);

    }

    @Override
    public void processDocMessage(Update update) {
        saveRawDate(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();

        if(isNotAllowToSendContent(chatId , appUser)) {
            return;
        }
            try {
                AppDocument doc = fileService.processDoc(update.getMessage());
                var answer = "Document upload successful! Url for download : http://test.orgget-doc/777  ";
                sendAnswer(answer, chatId);
            } catch (UploadFileException ex) {
                log.error(ex);
                String error = " Can't upload file,please try again later";
                sendAnswer(error, chatId);
            }
        }



    @Override
    public void processPhotoMessage(Update update) {
        saveRawDate(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();

        if(isNotAllowToSendContent(chatId , appUser)) {
            return;
            //TODO add save for photo
        }
        var  answer = "Photo upload successful! Url for download : http://test.orgget-photo/777  ";
        sendAnswer(answer , chatId);

    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
          var userState = appUser.getState();
          if(!appUser.getIsActive()){
              var error =  "Register or activate your account for download data!";
              sendAnswer(error , chatId);
              return true;
          } else if (!BASIC_STATE.equals(userState)) {
              var error = "Delayed your previous command , enter /cancel for send files.";
              sendAnswer(error,chatId);
              return true;
          }
           return false;
    }


    private void sendAnswer(String output,Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        var serviceCommand = ServiceCommands.fromValue(cmd);
        if(REGISTRATION.equals(serviceCommand)){

            //TODO add registration
            return "Temporally unavailable";
        }else if(HELP.equals(serviceCommand)){
            return help();
            
        } else if (START.equals(serviceCommand)) {
            return "Hello , to see list available command enter /help " ;
            
        }else {
            return "Unknown command!,to see list available command enter /help  ";
        }

    }

    private String help() {
        return "List available command :\n"
                 + "/cancel - cancel execute command\n"
                 + "/registration - register user";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Command is cancel!";
    }

    private AppUser findOrSaveAppUser( Update update){
            User  telegramUser = update.getMessage().getFrom();
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
