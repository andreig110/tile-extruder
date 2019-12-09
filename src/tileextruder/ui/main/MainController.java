package tileextruder.ui.main;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import tileextruder.model.TileExtruder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML // fx:id="vbox"
    private VBox vbox;

    @FXML // fx:id="saveMenuItem"
    private MenuItem saveMenuItem;

    @FXML // fx:id="saveBtn"
    private Button saveBtn;

    @FXML // fx:id="imageView"
    private ImageView imageView;

    @FXML // fx:id="extrudeBtn"
    private Button extrudeBtn;

    private FileChooser fileChooser;
    private BufferedImage image;

    @FXML
    void about(ActionEvent event) {
    }

    @FXML
    void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void open(ActionEvent event) {
        fileChooser.setTitle("Open Image");
        fileChooser.setInitialFileName("");
        File selectedFile = fileChooser.showOpenDialog(vbox.getScene().getWindow());
        if (selectedFile == null)
            return;

        try {
            image = ImageIO.read(selectedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateImageView();

        // Enable "extrude" & "save" controls
        saveMenuItem.setDisable(false);
        saveBtn.setDisable(false);
        extrudeBtn.setDisable(false);
    }

    private void updateImageView() {
        imageView.setImage(SwingFXUtils.toFXImage(image, null));
        imageView.setFitWidth(2 * image.getWidth());
        imageView.setFitHeight(2 * image.getHeight());
    }

    @FXML
    void extrude(ActionEvent event) {
        image = TileExtruder.extrudeTiles(image);
        updateImageView();
    }

    @FXML
    void save(ActionEvent event) {
        fileChooser.setTitle("Save Image");
        fileChooser.setInitialFileName("Untitled.png");
        File selectedFile = fileChooser.showSaveDialog(vbox.getScene().getWindow());
        if (selectedFile == null)
            return;

        // Check for the file extension. Add it if not specified
        String fileName = selectedFile.getName().toLowerCase();
        if (!fileName.endsWith(".png"))
            selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".png");

        try {
            ImageIO.write(image, "png", selectedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
    }

}
