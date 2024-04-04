package edu.java.bot.controller;

import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.service.SendUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BotController {
    private final SendUpdateService sendUpdateService;
    private static final String UPDATE_PROCESSED = "Обновление обработано";

    @PostMapping("/updates")
    public String doUpdate(@RequestBody @Valid LinkUpdateRequest linkUpdateRequest) {
        sendUpdateService.sendUpdateToUser(linkUpdateRequest);
        return UPDATE_PROCESSED;
    }
}
