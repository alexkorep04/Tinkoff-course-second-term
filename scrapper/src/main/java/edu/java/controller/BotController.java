package edu.java.controller;

import edu.java.dto.request.LinkUpdateRequest;
import edu.java.service.BotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotController {
    private final BotService botService;
    private static final String UPDATE_PROCESSED = "Обновление обработано";

    @PostMapping("/updates")
    public String doUpdate(@RequestBody @Valid LinkUpdateRequest linkUpdateRequest) {
        botService.addNewUpdate(linkUpdateRequest);
        return UPDATE_PROCESSED;
    }
}
