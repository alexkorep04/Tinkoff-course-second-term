package edu.java.bot.utils;

public class ValidLink {
    private ValidLink() {
    }

    @SuppressWarnings("MagicNumber")
    public static boolean isLinkNormal(String link) {
        return isGitHubLinkNormal(link) || isStackOverflowLinkNormal(link);
    }

    @SuppressWarnings("MagicNumber")
    private static boolean isGitHubLinkNormal(String link) {
        String[] parts = link.split("/");
        if (parts.length != 5) {
            return false;
        }
        return link.startsWith("https://github.com/")
            && !parts[3].isEmpty()
            && !parts[4].isEmpty();
    }

    @SuppressWarnings("MagicNumber")
    private static boolean isStackOverflowLinkNormal(String link) {
        String[] parts = link.split("/");
        if (parts.length != 6) {
            return false;
        }
        return link.startsWith("https://stackoverflow.com/questions/")
            && !parts[4].isEmpty()
            && isNumeric(parts[4])
            && !parts[5].isEmpty();
    }

    private static boolean isNumeric(String str) {
        try {
            long num = Long.parseLong(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
