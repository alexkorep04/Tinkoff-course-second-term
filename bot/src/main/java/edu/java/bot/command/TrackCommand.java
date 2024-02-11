package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import static edu.java.bot.utils.ValidLink.isLinkNormal;

@Log4j2
public class TrackCommand implements Command {
    private static final String LINK = "Link ";

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Start tracking the website page";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info(USER + update.message().chat().username() + " turned the track command");
        List<String> partsOfRequest = Arrays.stream(update.message().text().split(" ")).toList();
        if (partsOfRequest.size() != 2) {
            log.info(USER + update.message().chat().username() + " used invalid format of track command");
            return new SendMessage(update.message().chat().id(), "Invalid format! Please use /track <URI>");
        }
        if (!isLinkNormal(partsOfRequest.get(1))) {
            log.info(USER + update.message().chat().username() + " entered not correct URL " + partsOfRequest.get(1));
            return new
                SendMessage(update.message().chat().id(), LINK + partsOfRequest.get(1) + " is not supported now!");
        }
        log.info(USER + update.message().chat().username()
            + " successfully got response to track command to page " + partsOfRequest.get(1));
        return new SendMessage(update.message().chat().id(), LINK + partsOfRequest.get(1) + " is tracking!");
    }
}
