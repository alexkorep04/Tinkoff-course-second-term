package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import static edu.java.bot.service.DefaultLinkService.getAllLinksOfUser;

@Log4j2
public class ListCommand implements Command {
    private static final String NO_LINKS = "No tracked links!";

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Show all tracked links";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info(USER + update.message().chat().username() + " turned the list command");
        if (update.message().text().split(" ").length != 1) {
            return new SendMessage(update.message().chat().id(),
                "Invalid format! To see all tracked links use /list");
        }
        if (update.message().chat().id() == null) {
            return new SendMessage(update.message().chat().id(),
                NO_LINKS);
        }
        Set<String> links = getAllLinksOfUser(update.message().chat().id());
        if (!links.isEmpty()) {
            log.info(USER + update.message().chat().username() + " have tracked links");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("List of tracked links:").append("\n");
            for (String link: links) {
                stringBuilder.append(link).append("\n");
            }
            return new SendMessage(update.message().chat().id(), stringBuilder.toString());
        }
        log.info(USER + update.message().chat().username() + " does not have tracked links");
        return new SendMessage(update.message().chat().id(),
            NO_LINKS);
    }
}
