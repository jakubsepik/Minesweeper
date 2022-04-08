package app.minesweeper;

import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GameControler implements Initializable {
    @FXML
    private AnchorPane root;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Logic gamelogic = new Logic(5);
        root.getChildren().add(gamelogic.getGrid());

    }
}
