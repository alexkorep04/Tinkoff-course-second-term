package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import edu.java.bot.controller.MessageProcessor;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {
    @Bean
    public TelegramBot telegramBot(ApplicationConfig config) {
        return new TelegramBot(config.telegramToken());
    }

    @Bean
    public MessageProcessor messageProcessor() {
        List<Command> commands = List.of(
            new StartCommand(),
            new TrackCommand(),
            new UntrackCommand(),
            new ListCommand(),
            new HelpCommand()
        );
        return new MessageProcessor(commands);
    }
}
