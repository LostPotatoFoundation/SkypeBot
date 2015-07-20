package meh.SkypeBot.listeners;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

public interface SkypeListener extends ChatMessageListener {
    @Override
    default void chatMessageReceived(ChatMessage receivedChatMessage) throws SkypeException {
        handle(receivedChatMessage);
    }

    @Override
    default void chatMessageSent(ChatMessage sentChatMessage) throws SkypeException {
        handle(sentChatMessage);
    }

    void handle(ChatMessage chatMessage);

    default void stop(){
        System.exit(0);
    }
}
