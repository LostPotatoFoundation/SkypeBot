package Slayer.SkypeBot.Handlers;

import Slayer.SkypeBot.SkypeBot;
import com.skype.Chat;
import com.skype.SkypeException;

import java.io.File;
import java.util.List;
import java.util.Random;

public class Helper {
    public Random rand = new Random();

    public void init() {
        SkypeBot.cfg.handleConfig(Configurations.class, new File("SlayerBot.cfg"));
    }

    public void randomFunny(Chat chat, String target, String type) throws SkypeException {
        String message;
        int n;
        switch (type) {
            case("troll") :
                n = rand.nextInt(Configurations.trolls.size());
                message = "/me " + Configurations.trolls.get(n).replace("%%%", target);
                break;
            case("joke") :
                n = rand.nextInt(Configurations.jokes.size());
                message = Configurations.jokes.get(n);
                break;
            case("limerick") :
                n = rand.nextInt(Configurations.limericks.size());
                message = "/me " + Configurations.limericks.get(n).replace("@@", System.lineSeparator());
                break;
            default:
                message = "";
        }
        chat.send(message);
    }

    public void parseFromSaved(Chat chat, String message) throws SkypeException {
        String m = Configurations.commands.get(message.toLowerCase());
        if (!m.isEmpty())
            chat.send(m);
        else chat.send("No command exists with such a name.");
    }

    public String getCommands() {
        return "troll, joke, limerick, say, time, spam, " + Configurations.commands.keySet();
    }

    public boolean isOwner (String User) {
        return Configurations.owners.contains(User);
    }

    public static String getStringFromSList(List<String> lst) {
        final String[] string = {""};
        lst.forEach(o -> string[0] = string[0] + " " + o);
        return string[0];
    }
    public static String getStringFromIList(List<Integer> lst) {
        final String[] string = {""};
        lst.forEach(o -> string[0] = string[0] + " " + String.valueOf(o));
        return string[0];
    }
    public static String getStringFromBList(List<Boolean> lst) {
        final String[] string = {""};
        lst.forEach(o -> string[0] = string[0] + " " + String.valueOf(o));
        return string[0];
    }
    public static String getStringFromCList(List<Character> lst) {
        final String[] string = {""};
        lst.forEach(o -> string[0] = string[0] + " " + String.valueOf(o));
        return string[0];
    }
    public static String getStringFromFList(List<Float> lst) {
        final String[] string = {""};
        lst.forEach(o -> string[0] = string[0] + " " + String.valueOf(o));
        return string[0];
    }
}
