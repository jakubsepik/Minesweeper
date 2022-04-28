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
    private Button startButton,aboutButton,helpButton,exitButton, beginner, intermediate, expert;
    @FXML
    private Rectangle blurry,secondPanel,blurry2;
    private boolean vis;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vis = false;
    }

    /* handling buttons clicks */
    public void handleButtonAction(ActionEvent actionEvent) throws IOException {

        /* open scene with game when startButton is clicked */
        if(actionEvent.getSource()==startButton){
            if (!vis){
                beginner.setVisible(true);
                intermediate.setVisible(true);
                expert.setVisible(true);
                blurry2.setVisible(true);
                vis = true;
            }
            else {
                beginner.setVisible(false);
                intermediate.setVisible(false);
                expert.setVisible(false);
                blurry2.setVisible(false);
                vis = false;
            }
        }  else if(actionEvent.getSource()==helpButton){
            Parent root = FXMLLoader.load(getClass().getResource("helpView.fxml"));
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toString());
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } else if(actionEvent.getSource()==exitButton){
            Platform.exit();
        } else if (actionEvent.getSource()==beginner){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameView.fxml"));
            root = loader.load();
            GameControler gameControler = loader.getController();
            gameControler.setDifficulty(7);
            gameControler.startGame();
            gameControler.getLeader();
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toString());
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } else if (actionEvent.getSource()==intermediate){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameView.fxml"));
            root = loader.load();
            GameControler gameControler = loader.getController();
            gameControler.setDifficulty(12);
            gameControler.startGame();
            gameControler.getLeader();
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toString());
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } else if (actionEvent.getSource()==expert){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameView.fxml"));
            root = loader.load();
            GameControler gameControler = loader.getController();
            gameControler.setDifficulty(18);
            gameControler.startGame();
            gameControler.getLeader();
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toString());
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        }
    }
}