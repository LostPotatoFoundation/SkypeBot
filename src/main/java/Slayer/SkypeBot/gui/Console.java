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
   /* protected final TextField input = new TextField();

    protected final List<String> history = new ArrayList<>();
    protected int historyPointer = 0;
    protected int current = -1;*/


    public Console() {
        setBackground(Background.EMPTY);
        setStyle("-fx-background-color: black;");
        output.setFont(new Font("Segoe Script", 16));
        output.setWrapText(true);
        output.setStyle("-fx-text-fill: #00c9bc; -fx-background-color: black; -fx-border-color: black;-fx-control-inner-background: black;");
//        input.setFont(new Font("Segoe Script", 16));
//        input.setStyle("-fx-text-fill: #00c9bc; -fx-background-color: black; -fx-border-color: whitesmoke;-fx-control-inner-background: black;");
        output.setEditable(false);
        setCenter(output);
/*
        input.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                String text = input.getText();
                output.appendText("[" + "bot" + "] : " + text + System.lineSeparator());
                history.add(text);
                process(text);
                historyPointer++;
                if (onMessageReceivedHandler != null)
                    onMessageReceivedHandler.accept(text);
                current = 0;
                input.clear();
            } else if (keyEvent.getCode().equals(KeyCode.UP)) {
                current++;
                if (current < historyPointer) input.setText(history.get(historyPointer - current));
                else {
                    current = -1;
                    input.setText(history.get(historyPointer-current));
                }
            }
        });
        setBottom(input);*/
    }

 /*   @Override
    public void requestFocus() {
        super.requestFocus();
        input.requestFocus();
    }*/

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

/*
    private void clearHist() {
        history.clear();
        historyPointer = 0;
        current = -1;
    }*/
}