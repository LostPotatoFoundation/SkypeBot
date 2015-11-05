package Slayer.SkypeBot.Handlers;

import Slayer.SkypeBot.SkypeBot;
import com.skype.Chat;
import com.skype.SkypeException;

import java.util.Calendar;

public class MessageHandler {
    Helper parser = SkypeBot.helper;
    SkypeBot SlayerBot = SkypeBot.bot;
    public void processCommand(Chat chat, String message, String sender) throws SkypeException {
        if (message.startsWith("!")) {
            message = message.replaceFirst("!", "");
            boolean permitted = (parser.isOwner(sender) || !SlayerBot.lock);
            if (message.equalsIgnoreCase("help") || message.startsWith("command")) {
                chat.send(parser.getCommands());
                return;
            }
            if (permitted) {
                switch (message) {
                    case ("lock"):
                        SlayerBot.lock = true;
                        chat.send("Bot is locked!");
                        break;
                    case ("rl"):
                        parser.randomFunny(chat, sender, "limerick");
                        break;
                    case ("rj"):
                        parser.randomFunny(chat, sender, "joke");
                        break;
                    case ("limerick"):
                        parser.randomFunny(chat, sender, "limerick");
                        break;
                    case ("joke"):
                        parser.randomFunny(chat, sender, "joke");
                        break;
                    case ("time"):
                        chat.send(Calendar.getInstance().getTime().toString());
                        break;
                    default:
                        parser.parseFromSaved(chat, message);
                }

                if (message.startsWith("say")) {
                    message = message.replaceFirst("say", "").trim();
                    if (!message.startsWith("/") && !message.startsWith("!"))
                        chat.send(message);
                }
                if (message.startsWith("spam")) {
                    message = message.replaceFirst("spam", "").trim();
                    if (!message.startsWith("/") && !message.startsWith("!"))
                        for (int i = 0; i < 500; i++)
                            chat.send(message);
                }
                if (message.startsWith("rt")) {
                    String target = message.replace("rt", "").trim();
                    if (target.equals("")) target = sender;
                    parser.randomFunny(chat, target, "troll");
                }
                if (message.startsWith("troll")) {
                    String target = message.replace("troll", "").trim();
                    if (target.equals("")) target = sender;
                    parser.randomFunny(chat, target, "troll");
                }
            } else chat.send("Bot is locked and you are not a valid user.");

            if (parser.isOwner(sender)) {
                if (message.equalsIgnoreCase("quit")) {
                    chat.send("BOT: Shutting Down!");
                    System.exit(0);
                } else if (message.equalsIgnoreCase("unlock")) {
                    SlayerBot.lock = false;
                    chat.send("Bot is unlocked!");
                }
            }
        }
    }
}
