package fc.frontend;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DeleteIcon extends ImageView {

    private Image closed = new Image(this.getClass().getResourceAsStream("/images/icons/bin-icon-closed.png"));
    // private Image open = new
    // Image(this.getClass().getResourceAsStream("/images/icons/bin-icon-open.png"));

    public DeleteIcon() {

        setImage(closed);

    }

}
