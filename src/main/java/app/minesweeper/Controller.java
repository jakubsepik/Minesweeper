package app.minesweeper;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane backPanel;
    @FXML
    private Button startButton,aboutButton,helpButton,exitButton;
    @FXML
    private Rectangle blurry,secondPanel;


    @FXML
    protected void keyPressed(KeyEvent event){

    }

    @FXML
    protected void keyReleased(KeyEvent event){

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    /* handling buttons clicks */
    public void handleButtonAction(ActionEvent actionEvent) throws IOException {

        /* open scene with game when startButton is clicked */
        if(actionEvent.getSource()==startButton){
            Parent root = FXMLLoader.load(getClass().getResource("gameView.fxml"));
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if(actionEvent.getSource()==aboutButton){
            Parent root = FXMLLoader.load(getClass().getResource("aboutView.fxml"));
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if(actionEvent.getSource()==helpButton){
            Parent root = FXMLLoader.load(getClass().getResource("helpView.fxml"));
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if(actionEvent.getSource()==exitButton){
            Platform.exit();
        }
    }
}