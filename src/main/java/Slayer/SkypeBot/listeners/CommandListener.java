package Slayer.SkypeBot.listeners;

import Slayer.SkypeBot.SkypeBot;
import com.skype.ChatMessage;

public class CommandListener implements SkypeListener {
    public void handle(ChatMessage chatMessage) {
        try {
            if (SkypeBot.bot.console != null) {
                String text = chatMessage.getSenderDisplayName() + " : " + chatMessage.getContent();
                SkypeBot.bot.console.println(text);
            }
            SkypeBot.msgHandler.processCommand(chatMessage.getChat(), chatMessage.getContent(), chatMessage.getSenderDisplayName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
