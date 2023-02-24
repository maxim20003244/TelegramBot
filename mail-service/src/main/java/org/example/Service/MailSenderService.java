package org.example.Service;


import dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);
}
