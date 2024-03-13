package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.cllient.ScrapperClient;
import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.exception.LinkAlreadyExistsException;
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
public class TrackCommand implements Command {
    private final ScrapperClient scrapperClient;
    private static final String LINK = "Link ";

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Start tracking the website page";
    }

    @SuppressWarnings("ReturnCount")
    @Override
    public SendMessage handle(Update update) {
        log.info(USER + update.message().chat().username() + " turned the track command");
        List<String> partsOfRequest = Arrays.stream(update.message().text().split(" ")).toList();
        if (partsOfRequest.size() != 2) {
            log.info(USER + update.message().chat().username() + " used invalid format of track command");
            return new SendMessage(update.message().chat().id(), "Invalid format! Please use /track <URI>");
        }
        if (!isLinkNormal(partsOfRequest.get(1))) {
            log.info(USER + update.message().chat().username() + " entered not correct URI " + partsOfRequest.get(1));
            return new
                SendMessage(update.message().chat().id(), LINK + partsOfRequest.get(1) + " is not supported now!");
        }
        log.info(USER + update.message().chat().username()
            + " successfully got response to track command to page " + partsOfRequest.get(1));
        try {
            scrapperClient.addLink(update.message().chat().id(),
                new AddLinkRequest(URI.create(partsOfRequest.get(1)))).block();
            return new SendMessage(update.message().chat().id(), LINK
                + partsOfRequest.get(1) + " is tracking now!");
        } catch (LinkAlreadyExistsException e) {
            return new SendMessage(update.message().chat().id(), LINK + partsOfRequest.get(1)
                + " was already tracked earlier!");
        } catch (Exception e) {
            log.info(USER + update.message().chat().username() + " has exception in /track command");
            return null;
        }
    }
}
