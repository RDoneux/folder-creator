package fc.frontend;

import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CandidateViewer {

    private ListView<String> candidate;
    private ListView<ImageView> delete;
    private ListView<ImageView> edit;

    public CandidateViewer(ListView<String> candidate, ListView<ImageView> edit, ListView<ImageView> delete) {

        this.candidate = candidate;
        this.delete = delete;
        this.edit = edit;

        candidate.setEditable(true);
        candidate.setCellFactory(TextFieldListCell.forListView());

    }

    public void deleteCandidate() {
        int indexToDelete = delete.getSelectionModel().getSelectedIndex();
        delete.getSelectionModel().clearSelection(); // once index has been obtained, unselect item.
        candidate.getItems().remove(indexToDelete);
        delete.getItems().remove(indexToDelete);
    }

    public void addCandidate(String candidateName) {
        candidate.getItems().add(candidateName);
        delete.getItems().add(
                new ImageView(new Image(this.getClass().getResourceAsStream("/images/icons/bin-icon-closed.png"))));
    }
}
