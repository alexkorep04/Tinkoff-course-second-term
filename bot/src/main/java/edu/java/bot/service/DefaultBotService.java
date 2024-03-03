package edu.java.bot.service;

import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.exception.UpdateAlreadyExistsException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;

@Service
public class DefaultBotService implements BotService {
    private final List<LinkUpdateRequest> updates = new CopyOnWriteArrayList<>();
    private static final String UPDATE_IN_DB = "Update is already exists in database!";


    @Override
    public void addNewUpdate(LinkUpdateRequest linkUpdateRequest) {
        for (LinkUpdateRequest updateRequest: updates) {
            if (updateRequest.equals(linkUpdateRequest)) {
                throw new UpdateAlreadyExistsException(UPDATE_IN_DB);
            }
        }
        updates.add(linkUpdateRequest);
    }

}
