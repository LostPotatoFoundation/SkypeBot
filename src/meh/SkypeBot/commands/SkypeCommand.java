package meh.SkypeBot.commands;

import com.skype.Chat;
import com.skype.SkypeException;

import java.util.ArrayList;

public interface SkypeCommand {
    String getName();

    void execute(Chat chat, ArrayList<String> args) throws SkypeException;
}
