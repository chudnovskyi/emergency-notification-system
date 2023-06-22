package com.example.sender.services.telegram;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://api.telegram.org/", name = "TelegramAPI")
public interface TelegramApiClient {

    @GetMapping(value = "/bot{token}/sendMessage?chat_id={chatId}&text={content}")
    TelegramResponse sendMessage(@PathVariable String chatId, @PathVariable String content, @PathVariable String token);
}
