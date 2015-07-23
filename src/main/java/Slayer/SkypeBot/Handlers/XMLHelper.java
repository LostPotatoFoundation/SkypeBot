package Slayer.SkypeBot.Handlers;

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
    public Random rand = new Random();
    File inputFile;
    DocumentBuilderFactory dbf;

    public void init() {
        inputFile = new File("SlayerBot.xml");
        dbf = DocumentBuilderFactory.newInstance();
    }

    public String getStringWithAttr (String element, String Attr) {
        String tmp = "";
        if (element.equalsIgnoreCase("command")) tmp = "help, quit, lock, unlock, rl, rt, rj, owners, version, ";
        try {
            DocumentBuilder dB = dbf.newDocumentBuilder();
            Document doc = dB.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(element);

            List<String> message = new ArrayList<>();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    message.add(eElement.getAttribute(Attr));
                }
            }
            return tmp + message.toString().replace("[","").replace("]", "");
        } catch (Exception e) {e.printStackTrace(); return "No SlayerBot.xml found.";}
    }

    public List<String> getStringArrWithAttr (String element, String Attr) {
        try {
            DocumentBuilder dB = dbf.newDocumentBuilder();
            Document doc = dB.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(element);

            List<String> message = new ArrayList<>();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    message.add(eElement.getAttribute(Attr));
                }
            }
            return message;
        } catch (Exception e) {e.printStackTrace(); return null;}
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
//            if (isOwner(target)) return;
            DocumentBuilder dB = dbf.newDocumentBuilder();
            Document doc = dB.parse(inputFile);
            doc.getDocumentElement().normalize();

            int num = rand.nextInt(doc.getElementsByTagName(type).getLength());
            String message = doc.getElementsByTagName(type).item(num).getTextContent();
            message = message.replaceAll("%%%", target).replaceAll("@@", System.lineSeparator());
            if (type.equalsIgnoreCase("troll")) message = "/me " + message;
            chat.send(message);
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
/*
    private String testingMethod1(String string) throws SkypeException {
        List<String> args = getStringArrWithAttr("args", "arg");
        for (String arg : args) {
            if (string.contains(arg)) {
                procString(string, arg);
            }
        }
        return "";
    }


    public String procString(String string, String type) throws SkypeException {
        try {
            DocumentBuilder dB = dbf.newDocumentBuilder();
            Document doc = dB.parse(inputFile);
            doc.getDocumentElement().normalize();

            int num = rand.nextInt(doc.getElementsByTagName(type).getLength());
            String message = doc.getElementsByTagName(type).item(num).getTextContent();
            string = string.replaceAll(message, message);
            if (type.equalsIgnoreCase("troll")) {message = "/me " + message; chat.send(message);}
            else if (type.equalsIgnoreCase("joke"))chat.send(message);
            else if (type.equalsIgnoreCase("limerick")) {
                String[] LimerickParts = message.split("@@");
                for (String part : LimerickParts) {
                    chat.send(part.trim());
                }
            }
        } catch (Exception e) {e.printStackTrace(); chat.send("no SlayerBot.xml found!");}
    }*/

}
