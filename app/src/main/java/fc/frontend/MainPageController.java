package fc.frontend;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import fc.logic.FolderGenerator;
import javafx.collections.FXCollections;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainPageController {

    private Logger log = Logger.getLogger(MainPageController.class.getName());

    @FXML
    private TextField candidateName;

    @FXML
    private Button addButton;

    @FXML
    private TextFlow textFlow;
    public static DebugConsole debugConsole;

    @FXML
    private ListView<String> listViewer;

    @FXML
    private ListView<DeleteIcon> deleteColumn;
    private CandidateViewer candidateViewer;

    @FXML
    private Button createFoldersButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> courseSelector;
    private ObservableList<String> courses;

    @FXML
    public void initialize() {
        courses = FXCollections.observableArrayList("Introductory & Foundation", "Instructor Course", "Assessment Day",
                "Re-certification");
        courseSelector.setItems(courses);
        debugConsole = new DebugConsole(textFlow);
        candidateViewer = new CandidateViewer(listViewer, deleteColumn);
    }

    @FXML
    public void handleLogoClick() {
        Client.changeScene("settings-page");
    }

    @FXML
    public void handleAddCandidateButtonPress() {
        addCandidate();
    }

    @FXML
    public void handleAddCandidateKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            addCandidate();
        }
    }

    @FXML
    public void handleCreateFoldersButtonPress() {
        if (courseSelector == null || courseSelector.getValue() == null) {
            log.log(Level.SEVERE, "Course type not specified.");
            MainPageController.debugConsole.addText("Course type not specified", Color.RED);
            MainPageController.debugConsole.lineBreak();
            MainPageController.debugConsole.addText("Please specify the type of course that is scheduled", Color.RED);
            MainPageController.debugConsole.lineBreak();
            return;
        }
        if (datePicker == null || datePicker.getValue() == null) {
            log.log(Level.SEVERE, "Course date not specified.");
            MainPageController.debugConsole.addText("Course date not specified", Color.RED);
            MainPageController.debugConsole.lineBreak();
            MainPageController.debugConsole.addText("Please specify the start date of the course that is scheduled",
                    Color.RED);
            MainPageController.debugConsole.lineBreak();
            return;
        }
        if (listViewer == null || listViewer.getItems() == null) {
            log.log(Level.SEVERE, "No candidates identified");
            MainPageController.debugConsole.addText("No candidates identified.", Color.RED);
            MainPageController.debugConsole.lineBreak();
            MainPageController.debugConsole.addText("Please specify at least one candidate for this course", Color.RED);
            MainPageController.debugConsole.lineBreak();
            return;
        }
        new FolderGenerator().create(courseSelector.getValue(), datePicker.getValue().toString(),
                listViewer.getItems());
        listViewer.getItems().clear();
        deleteColumn.getItems().clear();
    }

    @FXML
    public void deleteCandidate() {
        candidateViewer.deleteCandidate();
    }

    private void addCandidate() {
        if (candidateName.getText().isEmpty()) {
            MainPageController.debugConsole.addText("Candidate name cannot be null", Color.RED);
            return;
        }
        if (listViewer.getItems().contains(candidateName.getText())) {
            MainPageController.debugConsole.addText("Candidate has already been added to course. Skipping...",
                    Color.ORANGE);
            candidateName.setText("");
            return;
        }
        candidateViewer.addCandidate(candidateName.getText());
        candidateName.setText("");
    }
}
