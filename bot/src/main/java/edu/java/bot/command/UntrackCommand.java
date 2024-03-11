package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.cllient.ScrapperClient;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.exception.NoResourceException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.ValidLink.isLinkNormal;

@Log4j2
@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private final ScrapperClient scrapperClient;

    private static final String LINK = "Link ";

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Stop tracking the website page";
    }

    @SuppressWarnings("ReturnCount")
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
                + " entered not correct URI for untracking " + link);
            return new SendMessage(update.message().chat().id(), LINK + link + " is not supported now!");
        }
        log.info(USER + update.message().chat().username()
            + " successfully got response to untrack command of page " + link);
        try {
            scrapperClient.deleteLink(update.message().chat().id(), new RemoveLinkRequest(URI.create(link))).block();
            return new SendMessage(update.message().chat().id(), LINK + link + " is not tracked now!");
        } catch (NoResourceException e) {
            return new SendMessage(update.message().chat().id(), LINK + link + " was not tracked earlier!");
        } catch (Exception e) {
            log.info(USER + update.message().chat().username() + " has exception in /untrack command");
            return null;
        }
    }
}
