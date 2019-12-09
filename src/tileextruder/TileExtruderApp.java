package tileextruder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TileExtruderApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ui/main/main.fxml"));
        primaryStage.setTitle("Tile Extruder");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
}
