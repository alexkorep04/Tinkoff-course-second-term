package edu.java.bot.bot;

public interface Bot extends AutoCloseable {
    void start();

    @Override
    void close();
}
