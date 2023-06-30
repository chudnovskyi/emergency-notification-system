package com.example.sender.services.telegram;

import com.example.sender.dto.response.TemplateHistoryResponse;
import feign.FeignException.BadRequest;
import feign.FeignException.Forbidden;
import feign.FeignException.TooManyRequests;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramAlertService {

    @Value("${notification.services.telegram.token}")
    private String token;

    private final TelegramApiClient telegramApiClient;

    public boolean sendMessage(String telegramId, TemplateHistoryResponse template) {
        String imageUrl = Optional.ofNullable(template.imageUrl()).orElse("");
        String content = Optional.ofNullable(template.content()).orElse("");
        String message = """
                %s
                                
                %s
                                
                %s
                """.formatted(template.title(), content, imageUrl);

        return send(telegramId, message);
    }

    private boolean send(String telegramId, String message) {
        try {
            TelegramResponse telegramResponse = telegramApiClient.sendMessage(telegramId, message, token);
            return telegramResponse.ok();
        } catch (BadRequest | Forbidden e) {
            return false; // TODO: 2 ways: user not found / user blocked bot
        } catch (TooManyRequests | RetryableException e) {
            log.warn("TooManyRequests to Telegram API, 10 seconds sleep");
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return send(telegramId, message);
        }
    }
}
