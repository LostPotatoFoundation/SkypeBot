package Slayer.SkypeBot;

import com.skype.Chat;
import com.skype.SkypeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class XMLHelper {
    public boolean lock;
    public Random rand = new Random();
    File inputFile;
    DocumentBuilderFactory dbf;

    public void init() {
        inputFile = new File("SlayerBot.xml");
        dbf = DocumentBuilderFactory.newInstance();
    }

    public void process (Chat chat, String command, String sender) throws SkypeException {
        boolean privileged = !lock || isOwner(sender);
        command = command.replaceFirst("!", "");
        if (command.equalsIgnoreCase("help")) chat.send(commandList());
        else if (privileged && command.equalsIgnoreCase("unlock")) {lock = false; chat.send("Bot is unlocked!");}
        else if (privileged && command.equalsIgnoreCase("lock")) {lock = true; chat.send("Bot is locked!");}
        else if (privileged && command.equalsIgnoreCase("quit")) {System.exit(0);}
        //spacer
        else if (command.equalsIgnoreCase("rl")) {randomFunny(chat, sender, "limerick");}
        else if (command.equalsIgnoreCase("rj")) {randomFunny(chat, sender, "joke");}
        else if (command.startsWith("say")) {chat.send(command.replaceFirst("say ", ""));}
        else if (command.startsWith("rt")) {
            String target = command.replace("rt", "").trim();
            if (target.equals("")) target = sender;
            randomFunny(chat, target, "troll");
        }
        else parseFromSaved(chat, command);
    }

    public String commandList () {
        try {
            DocumentBuilder dB = dbf.newDocumentBuilder();
            Document doc = dB.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("command");

            List<String> message = new ArrayList<>();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    message.add(eElement.getAttribute("commandName"));
                }
            }
            return "help, quit, lock, unlock, rl, rt, rj, " + message.toString().replace("[","").replace("]", "");
        } catch (Exception e) {e.printStackTrace(); return "help, quit, lock, unlock, meh, joke, and No SlayerBot.xml found.";}
    }

    public void parseFromSaved(Chat chat, String command) throws SkypeException {
        try {
            DocumentBuilder dB = dbf.newDocumentBuilder();
            Document doc = dB.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("command");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("commandName").equalsIgnoreCase(command))
                        chat.send(eElement.getElementsByTagName("response").item(0).getTextContent());
                }
            }
        } catch (Exception e) {e.printStackTrace(); chat.send("Command not recognised.");
        }
    }

    public void randomFunny(Chat chat, String target, String type) throws SkypeException {
        try {
            DocumentBuilder dB = dbf.newDocumentBuilder();
            Document doc = dB.parse(inputFile);
            doc.getDocumentElement().normalize();

            int num = rand.nextInt(doc.getElementsByTagName(type).getLength());
            String message = doc.getElementsByTagName(type).item(num).getTextContent();
            message = message.replaceAll("%%%", target);
            if (type.equalsIgnoreCase("troll")) {message = "/me " + message; chat.send(message);}
            else if (type.equalsIgnoreCase("joke"))chat.send(message);
            else if (type.equalsIgnoreCase("limerick")) {
                String[] LimerickParts = message.split("@@");
                for (String part : LimerickParts) {
                    chat.send(part.trim());
                }
            }
        } catch (Exception e) {e.printStackTrace(); chat.send("no SlayerBot.xml found!");}
    }

    public boolean isOwner (String User) {
        try {
            DocumentBuilder dB = dbf.newDocumentBuilder();
            Document doc = dB.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Owners");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("OwnerName").equalsIgnoreCase(User)) return true;
                }
            }
            return false;
        } catch (Exception e) {e.printStackTrace(); return false;}
    }
}
