package app.minesweeper;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
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
    private AnchorPane helpPanel,panel1,panel2;
    @FXML
    private ImageView topImg,a,r;


    private void zmena(){
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        System.out.println(screenBounds);
        if (screenBounds.getWidth()<1280 || screenBounds.getHeight() <900) {
            panel2.setMinWidth(screenBounds.getWidth()/2);
            panel1.setMinWidth(screenBounds.getWidth()/2);
            panel1.setScaleX(0.8);
            panel2.setScaleX(0.8);
            r.setScaleY(0.9);
            r.setScaleX(0.9);
            a.setScaleY(0.9);
            a.setScaleX(0.9);
            topImg.setScaleX(0.7);
            topImg.setScaleY(0.7);
            backButton.setScaleX(0.7);
            backButton.setScaleY(0.7);
            help1.setFont(Font.font ("dubai", 23));
            help2.setFont(Font.font ("dubai", 23));
            topImg.setFitWidth((screenBounds.getWidth()-topImg.getFitWidth())/1.5);
            AnchorPane.setLeftAnchor(topImg, 300.0);
            AnchorPane.setLeftAnchor(help1, 10.0);
            AnchorPane.setRightAnchor(help2, 10.0);
        }
        else {
            AnchorPane.setLeftAnchor(topImg, 550.0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        help1.setText("Welcome to a Minesweeper\n\nIn this game you are presented with a grid of boxes. You can find mines" +
                " or empty spaces in these boxes. Your goal is to reveal all empty boxes without revealing a single mine. " +
                "You can also place Flags to indicate a mine (this action won't detonate mine).\n\nTo make it competitive, " +
                "game will countdown your time till you finish revealing.");
        help2.setText("In this game your duty is to *click on the game grid and not hit a mine\n" +
                " *Left click - use to reveal box\n Right click - you will place \uD83D\uDEA9 " +
                "(use this as indicator where you think the mine is located)\n\n" +
                "If you click on the box and it is not a mine, a number will appear in it's place (this number" +
                " indicate number of mines in it's proximity ( 1 box to every side))");
        zmena();
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
