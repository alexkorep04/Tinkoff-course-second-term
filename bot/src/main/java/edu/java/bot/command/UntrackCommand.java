package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import static edu.java.bot.service.DefaultLinkService.deleteLink;
import static edu.java.bot.utils.ValidLink.isLinkNormal;

@Log4j2

public class UntrackCommand implements Command {

    private static final String LINK = "Link ";

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Stop tracking the website page";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info(USER + update.message().chat().username() + " turned the untrack command");
        List<String> partsOfRequest = Arrays.stream(update.message().text().split(" ")).toList();
        if (partsOfRequest.size() != 2) {
            return new SendMessage(update.message().chat().id(), "Invalid format! Please use /untrack <URI>!");
        }
        String link = partsOfRequest.get(1);
        if (!isLinkNormal(link)) {
            log.info(USER + update.message().chat().username()
                + " entered not correct URL for untracking " + link);
            return new SendMessage(update.message().chat().id(), LINK + link + " is not supported now!");
        }
        log.info(USER + update.message().chat().username() + " successfully got response to untrack command");
        boolean isDelete = deleteLink(update.message().chat().id(), link);
        if (!isDelete) {
            return new SendMessage(update.message().chat().id(), LINK + link + " is not in list of tracked links! Use correct <link>");
        }
        return new SendMessage(update.message().chat().id(), LINK + link + " is not tracked now!");
    }
}
