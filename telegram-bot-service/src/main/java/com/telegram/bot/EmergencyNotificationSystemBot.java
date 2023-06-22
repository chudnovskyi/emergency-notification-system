package com.telegram.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class EmergencyNotificationSystemBot extends TelegramLongPollingBot {

    private static final String INFO = "/info";

    @Value("${bot.name}")
    private String botUsername;

    public EmergencyNotificationSystemBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        if (update.getMessage().getText().equals(INFO)) {
            sendMessage(chatId, "ID for registration in the notification system: " + chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void sendMessage(Long chatId, String text) {
        String chatIdStr = String.valueOf(chatId);
        SendMessage sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred {}: {}", chatId, e.getMessage());
        }
    }
}
