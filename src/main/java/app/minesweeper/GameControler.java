package app.minesweeper;

import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GameControler implements Initializable {
    @FXML
    GridPane board;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Logic gamelogic = new Logic(5);


    }

    public void clickGrid(javafx.scene.input.MouseEvent event) {
        
        int x =GridPane.getColumnIndex((Node) event.getSource());
        int y =GridPane.getRowIndex((Node) event.getSource());
    }


}
