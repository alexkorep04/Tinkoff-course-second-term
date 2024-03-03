package edu.java.bot.service;

import edu.java.bot.dto.request.LinkUpdateRequest;

public interface BotService {
    void addNewUpdate(LinkUpdateRequest linkUpdateRequest);
}
