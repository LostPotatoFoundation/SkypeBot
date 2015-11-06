package Slayer.SkypeBot.gui;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.util.Objects;

import static Slayer.SkypeBot.gui.GUIUtils.runSafe;

@SuppressWarnings("unused")
public class Console extends BorderPane {
    protected final TextArea output = new TextArea();

    public Console() {
        setBackground(Background.EMPTY);
        setStyle("-fx-background-color: black;");
        output.setFont(new Font("Segoe Script", 16));
        output.setWrapText(true);
        output.setStyle("-fx-text-fill: #00c9bc; -fx-background-color: black; -fx-border-color: black;-fx-control-inner-background: black;");
        output.setEditable(false);
        setCenter(output);
    }

    public void clear() {
        runSafe(output::clear);
    }

    public void print(final String text) {
        Objects.requireNonNull(text, "text");
        runSafe(() -> output.appendText(text));
    }

    public void println(final String text) {
        Objects.requireNonNull(text, "text");
        runSafe(() -> output.appendText(text + System.lineSeparator()));
    }

    public void println() {
        runSafe(() -> output.appendText(System.lineSeparator()));
    }
}