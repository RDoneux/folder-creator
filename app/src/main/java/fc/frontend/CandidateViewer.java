package fc.frontend;

import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;

public class CandidateViewer {

    private ListView<String> candidate;
    private ListView<DeleteIcon> delete;

    public CandidateViewer(ListView<String> candidate, ListView<DeleteIcon> delete) {

        this.candidate = candidate;
        this.delete = delete;

        candidate.setEditable(true);
        candidate.setCellFactory(TextFieldListCell.forListView());

    }

    private void deleteCandidate(int index) {
        candidate.getItems().remove(index);
        delete.getItems().remove(index);
    }

    public void deleteCandidate() {
        int indexToDelete = delete.getSelectionModel().getSelectedIndex();
        delete.getSelectionModel().clearSelection(); // once index has been obtained, unselect item.
        deleteCandidate(indexToDelete);
    }

    public void addCandidate(String candidateName) {
        candidate.getItems().add(candidateName);
        delete.getItems().add(new DeleteIcon());
    }
}
