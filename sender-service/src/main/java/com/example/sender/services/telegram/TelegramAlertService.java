package com.example.sender.services.telegram;

import feign.FeignException.BadRequest;
import feign.FeignException.Forbidden;
import feign.FeignException.TooManyRequests;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramAlertService {

    @Value("${notification.services.telegram.token}")
    private String token;

    private final TelegramApiClient telegramApiClient;

    public boolean sendMessage(String chatId, String content) {
        try {
            TelegramResponse telegramResponse = telegramApiClient.sendMessage(chatId, content, token);
            return telegramResponse.ok();
        } catch (BadRequest | Forbidden e) {
            return false; // TODO: 2 realization: user not found / user blocked bot
        } catch (TooManyRequests | RetryableException e) {
            log.warn("TooManyRequests to Telegram API, 10 seconds sleep");
            try {
                Thread.sleep(10000L); // TODO: parallel
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return sendMessage(chatId, content);
        }
    }
}
