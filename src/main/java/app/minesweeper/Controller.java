package app.minesweeper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private AnchorPane backPanel;
    @FXML
    private Button startButton,aboutButton,helpButton,exitButton;


    @FXML
    protected void keyPressed(KeyEvent event){

    }

    @FXML
    protected void keyReleased(KeyEvent event){

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}