package app.minesweeper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GameControler implements Initializable {
    @FXML
    Text test;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            Logic gamelogic = new Logic(5);
            test.setText(Arrays.toString(gamelogic.getBoard()[0]));
    }
}
