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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpControler implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button backButton;
    @FXML
    private Label help1 , help2;
    @FXML
    private AnchorPane helpPanel;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        help1.setText("Welcome to a Minesweeper\nThis is a test");
        help2.setText("Welcome to a Minesweeper\nThis is a test");
    }

    /* handling buttons clicks */
    public void handleButtonAction(ActionEvent actionEvent) throws IOException {
        if(actionEvent.getSource()==backButton){
            Parent root = FXMLLoader.load(getClass().getResource("view.fxml"));
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toString());
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        }
    }
}
