package app.minesweeper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.tools.Borders;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameControler implements Initializable {
    @FXML
    public BorderPane rootBox;

    @FXML
    private Button leaveButton;
    private Stage stage;
    private Scene scene;
    private Parent rootP;
    private static final Integer STARTTIME = 0;
    private Timeline timeline;
    @FXML
    private Label counter;
    private Integer timeSeconds = STARTTIME;

    /* velkost hracej plochy */
    public int size;
    private double x, y;
    public Logic gamelogic;
    /* list s poliami kde su suradnice  uz zobrazenych policok */
    public Set<int[]> showed = new HashSet<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        size = 15;
        gamelogic = new Logic(size);
        rootBox.setCenter(getGrid());

        counter.setText(timeSeconds.toString());
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds = STARTTIME;

        // update timerLabel
        counter.setText(String.format("%02d:%02d", (timeSeconds % 3600) / 60, timeSeconds % 60));
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        timeSeconds++;
                        // update timerLabel
                        counter.setText(String.format("%02d:%02d", (timeSeconds % 3600) / 60, timeSeconds % 60));
                        if (timeSeconds >= 3600) {
                            timeline.stop();
                        }
                    }
                }));

        timeline.playFromStart();
    }

    public GridPane getGrid(){
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.CENTER);
        for(int i=0;i<size;i++){
            for(int o=0;o<size;o++){
                Button btn = new Button();
                btn.setOnAction(e -> {
                    int y =GridPane.getColumnIndex((Node) e.getSource());
                    int x =GridPane.getRowIndex((Node) e.getSource());
                    /* pridanie zobrazeneho policka do listu */
                    int [] cell = {x, y};
                    showed.add(cell);
                    /* update hracej plochy */
                    updateGrid(grid, x, y);
                });
                btn.getStyleClass().add("grid-btn");
                grid.add(btn, i, o);
            }
        }
        grid.getStyleClass().add("grid");
        return grid;
    }

    public void updateGrid(GridPane grid, int x, int y){
        GridPane newGrid = new GridPane();
        showed.addAll(gamelogic.getOne(x, y));
        newGrid.getStyleClass().add("grid");

        for(int i=0;i<size;i++){
            for(int o=0;o<size;o++){
                Button newBtn = showBtn(grid, i, o);
                newBtn.setOnAction(e -> {
                    int newY =GridPane.getColumnIndex((Node) e.getSource());
                    int newX =GridPane.getRowIndex((Node) e.getSource());
                    /* pridanie zobrazeneho policka do listu */
                    int [] cell = {x, y};
                    showed.add(cell);
                    /* update hracej plochy */
                    updateGrid(newGrid, newX, newY);
                });
                newBtn.getStyleClass().add("grid-btn");
                newGrid.add(newBtn, o, i);
            }
        }
        /* nahradenie hracej plochy novou */
        newGrid.getStyleClass().add("grid");
        rootBox.setCenter(newGrid);
    }

    public Button showBtn(GridPane grid, int x, int y) {
        Button newBtn = new Button();
        for (int[] cell : showed) {
            if (cell[0] == x && cell[1] == y) {
                if (gamelogic.getBoard()[x][y] == '0') {
                    newBtn = new Button();
                    newBtn.getStyleClass().add("zero-btn");
                } else {
                    newBtn = new Button(Character.toString(gamelogic.getBoard()[x][y]));
                }

                if (gamelogic.getBoard()[x][y] == '1') {
                    newBtn.getStyleClass().add("one-btn");
                }
                if (gamelogic.getBoard()[x][y] == '2') {
                    newBtn.getStyleClass().add("two-btn");
                }
                if (gamelogic.getBoard()[x][y] == '3') {
                    newBtn.getStyleClass().add("three-btn");
                }
                if (gamelogic.getBoard()[x][y] == '4') {
                    newBtn.getStyleClass().add("four-btn");
                }
                if (gamelogic.getBoard()[x][y] == '5') {
                    newBtn.getStyleClass().add("five-btn");
                }
                if (gamelogic.getBoard()[x][y] == '6') {
                    newBtn.getStyleClass().add("six-btn");
                }
                if (gamelogic.getBoard()[x][y] == '7') {
                    newBtn.getStyleClass().add("seven-btn");
                }
                if (gamelogic.getBoard()[x][y] == '8') {
                    newBtn.getStyleClass().add("eight-btn");
                }
            }
        }
        return newBtn;
    }


    public void handleButtonAction(ActionEvent actionEvent) throws IOException {
        if(actionEvent.getSource()==leaveButton){
            Parent rootP = FXMLLoader.load(getClass().getResource("view.fxml"));
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(rootP);
            scene.getStylesheets().add(getClass().getResource("style.css").toString());
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        }
    }
}
