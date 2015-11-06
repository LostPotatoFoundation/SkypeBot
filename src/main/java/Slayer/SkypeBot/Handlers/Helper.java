package Slayer.SkypeBot.Handlers;

import Slayer.SkypeBot.SkypeBot;
import com.skype.Chat;
import com.skype.SkypeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class Helper {
    public Random rand = new Random();
    private List<Integer> usedJokes = new ArrayList<>(), usedLims = new ArrayList<>(), usedTrls = new ArrayList<>();

    public void init() {
        SkypeBot.cfg.handleConfig(Configurations.class, SkypeBot.config);
    }

    public void randomFunny(Chat chat, String target, String type) throws SkypeException {
        String message;
        int n;
        if (usedTrls.size() == Configurations.trolls.size()) usedTrls = new ArrayList<>();
        if (usedLims.size() == Configurations.limericks.size()) usedLims = new ArrayList<>();
        if (usedJokes.size() == Configurations.jokes.size()) usedJokes = new ArrayList<>();
        switch (type) {
            case("troll") :
                n = rand.nextInt(Configurations.trolls.size());
                while (usedTrls.contains(n))
                    n = rand.nextInt(Configurations.trolls.size());
                message = "/me " + Configurations.trolls.get(n).replace("%%%", target);
                usedTrls.add(n);
                break;
            case("joke") :
                n = rand.nextInt(Configurations.jokes.size());
                while (usedJokes.contains(n))
                    n = rand.nextInt(Configurations.jokes.size());
                message = Configurations.jokes.get(n);
                usedJokes.add(n);
                break;
            case("limerick") :
                n = rand.nextInt(Configurations.limericks.size());
                while (usedLims.contains(n))
                    n = rand.nextInt(Configurations.limericks.size());
                message = Configurations.limericks.get(n).replace("@@", System.lineSeparator());
                usedLims.add(n);
                break;
            default:
                message = "";
        }
        if (!message.isEmpty())
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
