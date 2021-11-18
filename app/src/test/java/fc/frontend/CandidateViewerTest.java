package fc.frontend;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javafx.scene.control.ListView;
import javafx.embed.swing.JFXPanel;

public class CandidateViewerTest {

    private static ListView<String> CANDIDATE_LIST;
    private static ListView<DeleteIcon> DELETE_LIST;

    private static CandidateViewer CANDIDATE_VIEWER;

    private static final String CANDIDATE_ONE = "candidate-one";
    private static final String CANDIDATE_TWO = "candidate-two";

    @BeforeAll
    static void initialiseToolkit() {
        new JFXPanel(); // force FX to initialise tookit
    }

    @BeforeEach
    void init() {
        CANDIDATE_LIST = new ListView<String>();
        DELETE_LIST = new ListView<DeleteIcon>();
        CANDIDATE_VIEWER = new CandidateViewer(CANDIDATE_LIST, DELETE_LIST);
    }

    @Test
    void shouldAddCandidatesToListView() {
        assertTrue(CANDIDATE_LIST.getItems().isEmpty());

        CANDIDATE_VIEWER.addCandidate(CANDIDATE_ONE);
        CANDIDATE_VIEWER.addCandidate(CANDIDATE_TWO);

        assertTrue(CANDIDATE_LIST.getItems().contains(CANDIDATE_ONE));
        assertTrue(CANDIDATE_LIST.getItems().contains(CANDIDATE_TWO));
        assertTrue(DELETE_LIST.getItems().size() == 2);
    }

    @Test
    void shouldDeleteCandidateFromListView() {
        CANDIDATE_VIEWER.addCandidate(CANDIDATE_ONE);
        CANDIDATE_VIEWER.addCandidate(CANDIDATE_TWO);

        DELETE_LIST.getSelectionModel().select(1);

        CANDIDATE_VIEWER.deleteCandidate();

        assertTrue(CANDIDATE_LIST.getItems().contains(CANDIDATE_ONE));
        assertFalse(CANDIDATE_LIST.getItems().contains(CANDIDATE_TWO));
    }

}
