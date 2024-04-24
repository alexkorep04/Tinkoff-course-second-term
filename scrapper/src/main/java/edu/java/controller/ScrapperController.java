package edu.java.controller;

import edu.java.dto.Link;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListsLinkResponse;
import edu.java.service.LinkService;
import edu.java.service.TgChatService;
import io.micrometer.core.instrument.Counter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScrapperController {
    private final TgChatService tgChatService;
    private final LinkService linkService;
    private final Counter counter;
    private static final String CHAT_REGISTERED = "Чат зарегистрирован";
    private static final String CHAT_DELETED = "Чат успешно удалён";

    @PostMapping("/tg-chat/{id}")
    public String registerChat(@PathVariable("id") @Min(1) long id) {
        counter.increment();
        tgChatService.register(id);
        return CHAT_REGISTERED;
    }

    @DeleteMapping("/tg-chat/{id}")
    public String deleteChat(@PathVariable("id") @Min(1) long id) {
        counter.increment();
        tgChatService.unregister(id);
        return CHAT_DELETED;
    }

    @GetMapping("/links")
    private ListsLinkResponse getAllLinks(@RequestHeader("Tg-Chat-Id") @Min(1) long id) {
        counter.increment();
        List<Link> links = linkService.listAll(id);
        List<LinkResponse> linkResponses = new ArrayList<>();
        for (Link link: links) {
            linkResponses.add(new LinkResponse(link.getId(), URI.create(link.getName())));
        }
        return new ListsLinkResponse(linkResponses, links.size());
    }

    @PostMapping("/links")
    private LinkResponse addLink(@RequestHeader("Tg-Chat-Id") @Min(1) long id,
        @RequestBody @Valid AddLinkRequest addLinkRequest) {
        counter.increment();
        Link link = linkService.add(id, addLinkRequest.getLink());
        return new LinkResponse(link.getId(), URI.create(link.getName()));
    }

    @DeleteMapping("/links")
    private LinkResponse deleteLink(@RequestHeader("Tg-Chat-Id") @Min(1) long id,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest) {
        counter.increment();
        Link link = linkService.remove(id, removeLinkRequest.getLink());
        return new LinkResponse(link.getId(), URI.create(link.getName()));
    }
}
