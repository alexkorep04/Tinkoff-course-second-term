package edu.java.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BaseURL {
    GITHUB("https://api.github.com"),
    STACKOVERFLOW("https://api.stackexchange.com/2.3"),
    LOCAL("http://localhost:8080");
    private final String url;
}
