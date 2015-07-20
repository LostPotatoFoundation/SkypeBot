package Slayer.SkypeBot.commands;

import com.skype.Chat;
import com.skype.SkypeException;

import java.util.ArrayList;

public class CommandSpam implements SkypeCommand {
    @Override
    public String getName() {
        return "spam";
    }

    @Override
    public void execute(Chat chat, ArrayList<String> args) throws SkypeException {
        int amount = Integer.parseInt(args.get(0));
        String message = args.get(1);

        for (int i = 0; i < amount; i++)
            chat.send(message);
    }
}
