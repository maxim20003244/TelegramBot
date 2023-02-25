package org.example.impl;

import dto.MailParams;
import lombok.extern.log4j.Log4j;
import org.example.CryptoTool;
import org.example.dao.AppUserDAO;
import org.example.entity.AppUser;
import org.example.entity.enums.UserState;
import org.example.service.AppUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
@Log4j
@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserDAO appUserDAO;
    private final CryptoTool cryptoTool;
    @Value("${service.mail.uri}")
  private String mailServiceUri;



    public AppUserServiceImpl(AppUserDAO appUserDAO, CryptoTool cryptoTool) {
        this.appUserDAO = appUserDAO;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public String registerUser(AppUser appUser) {
        if(appUser.getIsActive()){
            return "You are already register";
        }else if(appUser.getEmail()!= null){
            return "On your email already was sends a email"
                    + "Go to link in a letter to finished registration ";
        }
       appUser.setState(UserState.WAIT_FOR_EMAIL_STATE);
        appUserDAO.save(appUser);
        return "Enter please your email";
    }


    @Override
    public String sendEmail(AppUser appUser, String email) {
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            return "Enter please correct email. For cancel command enter /cancel";
        }
        var optional = appUserDAO.findByEmail(email);
        if(optional.isEmpty()){
            appUser.setEmail(email);
            appUser.setState(UserState.BASIC_STATE);
            appUser = appUserDAO.save(appUser);

            var cryptoUserId = cryptoTool.hashOf(appUser.getId());
            var response = sendRequestToMailService (cryptoUserId, email);
            if(response.getStatusCode()!= HttpStatus.OK){
            var msg = String.format("Send letter on email is can't be completed", email);
             log.error(msg);
             appUser.setEmail(null);
             appUserDAO.save(appUser);
             return msg;
            }
            return "On your email already was sends a email"
                    + "Go to link in a letter to accept registration  ";
        }else {
        return "This email already exist .ENter a correct email."+
          "For cancel enter /cancel" ;
        }
    }


    private ResponseEntity<String>sendRequestToMailService(String cryptoUserId, String email) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var mailParams = MailParams.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();

        var request = new HttpEntity<>(mailParams,headers);
        return restTemplate.exchange(mailServiceUri,
                HttpMethod.POST,
                request,
                String.class);
    }
    }
