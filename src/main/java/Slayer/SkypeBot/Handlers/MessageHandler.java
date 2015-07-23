package Slayer.SkypeBot.Handlers;

import Slayer.SkypeBot.SkypeBot;
import com.skype.Chat;
import com.skype.SkypeException;

import java.util.Calendar;

public class MessageHandler {
    XMLHelper parser = SkypeBot.xmlHelper;
    SkypeBot SlayerBot = SkypeBot.bot;
    public void processCommand(Chat chat, String message, String sender) throws SkypeException {
        message = message.replaceFirst("!", "");
        boolean permitted = (parser.isOwner(sender) || !SlayerBot.lock);
        if (message.equalsIgnoreCase("help") || message.equalsIgnoreCase("command")) {chat.send(parser.getStringWithAttr("command" ,"commandName")); return;}
        if (message.equalsIgnoreCase("version")) {chat.send("SlayerSkypeBot Version : Alpha 1 update 134"); return;}
        if (message.equalsIgnoreCase("owners")) {chat.send(parser.getStringWithAttr("Owners", "OwnerName")); return;}
        if (permitted) {
            switch (message) {
                case("lock") :
                    SlayerBot.lock = true;
                    chat.send("Bot is locked!");
                    break;
                case("rl") :
                    parser.randomFunny(chat, sender, "limerick");
                    break;
                case("rj") :
                    parser.randomFunny(chat, sender, "joke");
                    break;
                case("time") :
                    chat.send(Calendar.getInstance().getTime().toString());
                    break;
                default: parser.parseFromSaved(chat, message);
            }

            if (message.startsWith("say")) {
                message = message.replaceFirst("say", "").trim();
                if (!message.startsWith("/") && !message.startsWith("!"))
                    chat.send(message);
            }
            if (message.startsWith("rt")) {
                String target = message.replace("rt", "").trim();
                if (target.equals("")) target = sender;
                parser.randomFunny(chat, target, "troll");
            }
        }
        else chat.send("Bot is locked and you are not a valid user.");

        if (parser.isOwner(sender)) {
            if (message.equalsIgnoreCase("quit")) {chat.send("BOT: Shutting Down!");System.exit(0);}
            else if (message.equalsIgnoreCase("unlock")) {SlayerBot.lock = false; chat.send("Bot is unlocked!");}
        }
    }
}
