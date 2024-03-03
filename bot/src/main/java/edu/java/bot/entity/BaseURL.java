package edu.java.bot.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BaseURL {
    LOCAL("http://localhost:8080");
    private final String url;
}
