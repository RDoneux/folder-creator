package fc.frontend;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class DebugConsoleTest {

    private static TextFlow TEXT_FLOW;

    private static DebugConsole DEBUG_CONSOLE;

    private static final String TEXT_INPUT = "text-input";

    @BeforeAll()
    static void initaliseToolkit() {
        new JFXPanel();
    }

    @BeforeEach()
    void init() {
        TEXT_FLOW = new TextFlow();
        DEBUG_CONSOLE = new DebugConsole(TEXT_FLOW);
    }

    @Test
    void shouldAddTextToTextFlow() {
        DEBUG_CONSOLE.addText(TEXT_INPUT);
        assertTrue(extractText().contains(TEXT_INPUT));
    }

    @Test
    void shouldSetCorrectNodeColour() {
        DEBUG_CONSOLE.addText(TEXT_INPUT, Color.GREEN);
        assertTrue(extractText().contains(TEXT_INPUT));
        assertTrue(((Text)getTextNode(0)).getFill() == Color.GREEN);
    }

    private String extractText() {
        StringBuilder builder = new StringBuilder();
        for (Node text : TEXT_FLOW.getChildren()) {
            builder.append(((Text) text).getText());
        }
        return builder.toString();
    }

    private Node getTextNode(int index) {
        return TEXT_FLOW.getChildren().get(index);
    }

}
