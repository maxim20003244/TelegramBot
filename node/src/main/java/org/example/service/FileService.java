package org.example.service;

import org.example.entity.AppDocument;
import org.example.entity.AppPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;


public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto (Message telegramMessage);
}
