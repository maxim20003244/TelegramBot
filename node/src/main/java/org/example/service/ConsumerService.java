package org.example.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {
    void consumeTextMessageUpdate(Update udate);
    void consumeDocMessageUpdate(Update update);
    void consumePhotoMessageUpdate(Update update);
}
