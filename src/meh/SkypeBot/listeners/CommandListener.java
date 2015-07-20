package meh.SkypeBot.listeners;

import com.skype.*;
import meh.SkypeBot.SkypeBot;
import meh.SkypeBot.XMLHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandListener implements SkypeListener {
    public void handle(ChatMessage chatMessage) {
        try {
            if (SkypeBot.bot.console != null) {
                String text = chatMessage.getSenderDisplayName() + " : " + chatMessage.getContent();
                SkypeBot.bot.console.println(text);
            }

            String[] strings = chatMessage.getContent().split(" ");

            if (strings[0].charAt(0) == '!') {
                try {
                    String command = strings[0].replace("!", "");

                    ArrayList<String> args = new ArrayList<>();
                    args.addAll(Arrays.asList(strings).subList(1, strings.length));

                    SkypeBot.commands.containsKey(command);
                    SkypeBot.commands.get(command).execute(chatMessage.getChat(), args);
                } catch (Exception ingored) {
                    SkypeBot.xmlHelper.process(chatMessage.getChat(), chatMessage.getContent(), chatMessage.getSenderDisplayName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
