package meh.SkypeBot;

import javafx.scene.Group;
import meh.SkypeBot.gui.*;

import com.skype.Skype;
import com.skype.SkypeException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import meh.SkypeBot.commands.SkypeCommand;
import meh.SkypeBot.listeners.SkypeListener;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SkypeBot extends Application {
    public static HashMap<String, SkypeCommand> commands = new HashMap<>();
    private static ArrayList<SkypeListener> chatMessageListeners = new ArrayList<>();
    public static SkypeBot bot;
    public Console console;

    public static void main(String[] args) throws SkypeException{
        registerCommands();

        registerMessageListeners();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        bot = this;
        console = new Console();
        Group root = new Group(console);
        Scene scene = new Scene(root, 678, 272);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Skype Bot");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        chatMessageListeners.forEach(Skype::removeChatMessageListener);
        chatMessageListeners.forEach(SkypeListener::stop);
    }

    private static void registerCommands(){
        Reflections commandsR = new Reflections("meh.SkypeBot.commands");
        Set<Class<? extends SkypeCommand>> commandsS =  commandsR.getSubTypesOf(SkypeCommand.class);

        for(Class<? extends SkypeCommand> commandClass : commandsS){
            try {
                SkypeCommand skypeCommand = commandClass.newInstance();
                commands.put(skypeCommand.getName(), skypeCommand);

                System.out.println("Registered command: " + skypeCommand);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void registerMessageListeners(){
        Reflections listenR = new Reflections("meh.SkypeBot.listeners");
        Set<Class<? extends SkypeListener>> listenS = listenR.getSubTypesOf(SkypeListener.class);

        for (Class<? extends SkypeListener> listenClass : listenS){
            try {
                SkypeListener skypeListener = listenClass.newInstance();
                Skype.addChatMessageListener(skypeListener);
                chatMessageListeners.add(skypeListener);

                System.out.println("Registered listener: " + skypeListener);
            } catch (SkypeException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
