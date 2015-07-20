package Slayer.SkypeBot.commands;

import com.skype.Chat;
import com.skype.SkypeException;

import java.util.ArrayList;
import java.util.Calendar;

public class CommandTime implements SkypeCommand {
    @Override
    public String getName() {
        return "time";
    }

    @Override
    public void execute(Chat chat, ArrayList<String> args) throws SkypeException {
        chat.send(Calendar.getInstance().getTime().toString());
    }
}
