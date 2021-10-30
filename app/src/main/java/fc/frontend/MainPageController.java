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
import javafx.scene.text.TextFlow;
import fc.logic.FolderGenerator;
import javafx.collections.FXCollections;

public class MainPageController {

    @FXML
    private TextField candidateName;

    @FXML
    private Button addButton;

    @FXML
    private TextFlow textFlow;

    @FXML
    private ListView<String> listViewer;

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
        new FolderGenerator().create(courseSelector.getValue(), datePicker.getValue().toString(),
                listViewer.getItems());
    }

    private void addCandidate() {
        if (!candidateName.getText().isEmpty() && !listViewer.getItems().contains(candidateName.getText())) {
            listViewer.getItems().add(candidateName.getText());
            candidateName.setText("");
        }
    }
}
