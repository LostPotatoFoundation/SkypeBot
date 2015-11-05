package Slayer.SkypeBot;

import Configuration.HxCConfig;
import Slayer.SkypeBot.Handlers.MessageHandler;
import Slayer.SkypeBot.Handlers.Helper;
import Slayer.SkypeBot.gui.Console;
import Slayer.SkypeBot.listeners.SkypeListener;
import com.skype.Skype;
import com.skype.SkypeException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Set;

public class SkypeBot extends Application {
    public static String VERSION = "v1.1.0";
    private static ArrayList<SkypeListener> chatMessageListeners = new ArrayList<>();
    public static SkypeBot bot;
    public Console console;
    public static Helper helper;
    public boolean lock = false;
    public static MessageHandler msgHandler;
    public static HxCConfig cfg = new HxCConfig();

    public static void main(String[] args) {
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
        helper = new Helper();
        helper.init();
        msgHandler = new MessageHandler();
    }

    @Override
    public void stop() throws Exception {
        chatMessageListeners.forEach(Skype::removeChatMessageListener);
        chatMessageListeners.forEach(SkypeListener::stop);
    }

    private static void registerMessageListeners(){
        Reflections listenR = new Reflections("Slayer.SkypeBot.listeners");
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
