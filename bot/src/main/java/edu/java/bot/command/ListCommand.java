package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.cllient.ScrapperClient;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListsLinkResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private final ScrapperClient scrapperClient;
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
            log.info(USER + update.message().chat().username()
                + " turned the list command with not correct parameters");
            return new SendMessage(update.message().chat().id(),
                "Invalid format! To see all tracked links use /list");
        }
        try {
            ListsLinkResponse linkResponse = scrapperClient.getLinks(update.message().chat().id()).block();
            if (linkResponse.getSize() == 0) {
                log.info(USER + update.message().chat().username() + " does not have tracked links");
                return new SendMessage(update.message().chat().id(), NO_LINKS);
            }
            List<LinkResponse> linkResponses = linkResponse.getLinks();
            List<String> links = new ArrayList<>();
            for (LinkResponse link: linkResponses) {
                links.add(link.getUrl().toString());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("List of tracked links:").append("\n");
            for (String link: links) {
                stringBuilder.append(link).append("\n");
            }
            log.info(USER + update.message().chat().username() + " have tracked links");
            return new SendMessage(update.message().chat().id(), stringBuilder.toString());
        } catch (Exception e) {
            log.info(USER + update.message().chat().username() + " has exception in /list command");
            return null;
        }
    }
}
