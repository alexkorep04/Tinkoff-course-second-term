package edu.java.bot.utils;

public class ValidLink {
    private ValidLink() {
    }

    public static boolean isLinkNormal(String link) {
        return link.startsWith("https://github.com/") || link.startsWith("https://stackoverflow.com/");
    }
}
