package tileextruder.ui.main;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tileextruder.model.TileExtruder;
import tileextruder.util.FXImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.UnaryOperator;

public class MainController {

    @FXML // fx:id="vbox"
    private VBox vbox;

    @FXML // fx:id="saveMenuItem"
    private MenuItem saveMenuItem;

    @FXML // fx:id="saveBtn"
    private Button saveBtn;

    @FXML // fx:id="imageView"
    private ImageView imageView;

    @FXML // fx:id="rightPanel"
    private VBox rightPanel;

    @FXML // fx:id="tileWidth"
    private TextField tileWidthText;

    @FXML // fx:id="tileHeight"
    private TextField tileHeightText;

    @FXML // fx:id="extrudeBtn"
    private Button extrudeBtn;

    @FXML // fx:id="zoomChoiceBox"
    private ChoiceBox<String> zoomChoiceBox;

    private FileChooser fileChooser;
    private BufferedImage bufferedImage;
    private int zoomFactor = 100;
    private boolean controlsDisabled = true;

    @FXML
    void initialize() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"));

        UnaryOperator<TextFormatter.Change> tileSizeFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([1-9][0-9]*)?")) {
                if ((newText.length() < 4) || (Integer.parseInt(newText) <= 1024)) {
                    return change;
                }
            }
            return null;
        };
        tileWidthText.setTextFormatter(new TextFormatter<>(tileSizeFilter));
        tileHeightText.setTextFormatter(new TextFormatter<>(tileSizeFilter));

        zoomChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    zoomFactor = Integer.parseInt( newValue.substring(0, newValue.length() - 2) );
                    updateImageView();
                });
    }

    @FXML
    void about(ActionEvent event) throws IOException {
        Stage aboutStage = new Stage();
        aboutStage.initOwner(vbox.getScene().getWindow());
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.initStyle(StageStyle.UTILITY);
        aboutStage.setTitle("About Tile Extruder");
        Parent root = FXMLLoader.load(getClass().getResource("../about/about.fxml"));
        aboutStage.setScene(new Scene(root, 400, 180));
        aboutStage.setResizable(false);
        aboutStage.showAndWait();
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
            bufferedImage = ImageIO.read(selectedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateImageView();

        // Enable controls if disabled
        if (controlsDisabled) {
            saveMenuItem.setDisable(false);
            saveBtn.setDisable(false);
            rightPanel.setDisable(false);
            zoomChoiceBox.setDisable(false);
            controlsDisabled = false;
        }
    }

    private void updateImageView() {
        Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
        final double scaleFactor = zoomFactor / 100d;

        switch (zoomFactor) {
            case 25:
            case 50:
            case 100:
                imageView.setImage(fxImage);
                break;
            case 200:
            case 400:
                imageView.setImage(FXImageUtil.scaleImage(fxImage, (int) scaleFactor));
        }

        imageView.setFitWidth(scaleFactor * bufferedImage.getWidth());
        imageView.setFitHeight(scaleFactor * bufferedImage.getHeight());
    }

    @FXML
    void extrude(ActionEvent event) {
        bufferedImage = TileExtruder.extrudeTiles(bufferedImage, Integer.parseInt(tileWidthText.getText()),
                Integer.parseInt(tileHeightText.getText()));
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
            ImageIO.write(bufferedImage, "png", selectedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
