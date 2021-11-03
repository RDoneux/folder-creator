package fc.frontend;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class DebugConsole extends TextFlow {

    private TextFlow textFlow;

    public DebugConsole(TextFlow textFlow) {
        this.textFlow = textFlow;
    }

    public void addText(String text) {
        textFlow.getChildren().add(new Text(text));
        textFlow.getChildren().add(new Text(System.lineSeparator()));
    }

    public void addText(String text, Color colour) {
        Text input = new Text(text);
        input.setFill(colour);
        textFlow.getChildren().add(input);
        textFlow.getChildren().add(new Text(System.lineSeparator()));
    }

    public void lineBreak() {
        textFlow.getChildren().add(new Text(System.lineSeparator()));
    }

}
