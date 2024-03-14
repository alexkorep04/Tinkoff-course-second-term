package edu.java.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BaseURL {
    GITHUB("https://api.github.com"),
    STACKOVERFLOW("https://api.stackexchange.com/2.3");

    private final String url;
}
