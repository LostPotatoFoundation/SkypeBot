package Slayer.SkypeBot.commands;

import Slayer.SkypeBot.SkypeBot;
import Slayer.SkypeBot.XMLHelper;
import com.skype.Chat;
import com.skype.SkypeException;

public class MessageHandler {
    XMLHelper parser = SkypeBot.xmlHelper;
    SkypeBot SlayerBot = SkypeBot.bot;
    public void processCommand(Chat chat, String message, String sender) throws SkypeException {
        message = message.replaceFirst("!", "");
        boolean permitted = (parser.isOwner(sender) || !SlayerBot.lock);
        if (message.equalsIgnoreCase("help")) {chat.send(parser.getAttr("command" ,"commandName")); return;}
        if (message.equalsIgnoreCase("version")) {chat.send("SlayerSkypeBot Version : Alpha 1 update 131"); return;}
        if (message.equalsIgnoreCase("owners")) {chat.send(parser.getAttr("Owners", "OwnerName")); return;}
        if (permitted) {
            if (message.equalsIgnoreCase("lock")) {SlayerBot.lock = true; chat.send("Bot is locked!");}
            else if (message.equalsIgnoreCase("rl")) {parser.randomFunny(chat, sender, "limerick");}
            else if (message.equalsIgnoreCase("rj")) {parser.randomFunny(chat, sender, "joke");}
            else if (message.startsWith("say") && !(message.startsWith("say /") || message.startsWith("say !"))) {chat.send(message.replaceFirst("say ", ""));}
            else if (message.startsWith("rt")) {
                String target = message.replace("rt", "").trim();
                if (target.equals("")) target = sender;
                parser.randomFunny(chat, target, "troll");
            }
            else parser.parseFromSaved(chat, message);
        }
        else chat.send("Bot is locked and you are not a valid user.");

        if (parser.isOwner(sender)) {
            if (message.equalsIgnoreCase("quit")) {chat.send("BOT: Shutting Down!");System.exit(0);}
            else if (message.equalsIgnoreCase("unlock")) {SlayerBot.lock = false; chat.send("Bot is unlocked!");
            }
        }
    }
}
