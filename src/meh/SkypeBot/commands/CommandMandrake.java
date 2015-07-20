package meh.SkypeBot.commands;

import com.skype.Chat;
import com.skype.SkypeException;

import java.util.ArrayList;

public class CommandMandrake implements SkypeCommand {
    @Override
    public String getName() {
        return "mandrake";
    }

    @Override
    public void execute(Chat chat, ArrayList<String> args) throws SkypeException{
        chat.send("IDK about him but his mom is stupid and an asshole!");
    }
}
