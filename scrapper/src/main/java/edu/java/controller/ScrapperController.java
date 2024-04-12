package edu.java.controller;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListsLinkResponse;
import edu.java.service.ScrapperService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    private final ScrapperService scrapperService;
    private static final String CHAT_REGISTERED = "Чат зарегистрирован";
    private static final String CHAT_DELETED = "Чат успешно удалён";

    @PostMapping("/tg-chat/{id}")
    public String registerChat(@PathVariable("id") @Min(1) long id) {
        scrapperService.registerChat(id);
        return CHAT_REGISTERED;
    }

    @DeleteMapping("/tg-chat/{id}")
    public String deleteChat(@PathVariable("id") @Min(1) long id) {
        scrapperService.deleteChat(id);
        return CHAT_DELETED;
    }

    @GetMapping("/links")
    private ListsLinkResponse getAllLinks(@RequestHeader("Tg-Chat-Id") @Min(1) long id) {
        List<LinkResponse> links = scrapperService.getLinks(id);
        return new ListsLinkResponse(links, links.size());
    }

    @PostMapping("/links")
    private LinkResponse addLink(@RequestHeader("Tg-Chat-Id") @Min(1) long id,
        @RequestBody @Valid AddLinkRequest addLinkRequest) {
        return scrapperService.addLink(id, addLinkRequest.getLink());
    }

    @DeleteMapping("/links")
    private LinkResponse deleteLink(@RequestHeader("Tg-Chat-Id") @Min(1) long id,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest) {
        return scrapperService.deleteLink(id, removeLinkRequest.getLink());
    }
}
