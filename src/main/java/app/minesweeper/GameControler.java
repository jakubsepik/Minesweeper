package app.minesweeper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    public HBox rootBox;

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
    public Logic gamelogic;
    /* list s poliami kde su suradnice uz zobrazenych policok */
    public Set<int[]> showed = new HashSet<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        size = 6;
        gamelogic = new Logic(size);
        rootBox.getChildren().add(getGrid());

        counter.setText(timeSeconds.toString());
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds = STARTTIME;

        // update timerLabel
        counter.setText(String.format("%02d:%02d", (timeSeconds % 3600) / 60, timeSeconds % 60));
        counter.setStyle(
                "-fx-font-size: 16;" +
                "-fx-text-fill: #fff"
        );
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
                btn.setPrefHeight(50);
                btn.setPrefWidth(50);
                grid.add(btn, i, o);
            }
        }
        return grid;
    }

    public void updateGrid(GridPane grid, int x, int y){
        List<int[]> toShow = gamelogic.getOne(x, y);
        GridPane newGrid = new GridPane();
        newGrid.setPadding(new Insets(10, 10, 10, 10));
        for(int i=0;i<size;i++){
            for(int o=0;o<size;o++){
                Button newBtn = new Button();

                /* zobrazenie policok ktore vrati metoda getOne */
                for (int t = 0; t < toShow.size(); t++){
                    if (toShow.get(t)[0] == i && toShow.get(t)[1] == o){
                        newBtn = new Button(Character.toString(gamelogic.getBoard()[i][o]));
                        int [] cell = {i, o};
                        showed.add(cell);
                    }
                }

                /* zobrazenie vsetkych policok, ktore maju byt zobrazene */
                for (int [] cell: showed){
                    if (cell[0] == i && cell[1] == o){
                        if (gamelogic.getBoard()[i][o] == '0'){
                            newBtn = new Button();
                            newBtn.setStyle(
                                    "-fx-background-color: rgba(153,153,153,0.05);"
                                    );
                        } else {
                            newBtn = new Button(Character.toString(gamelogic.getBoard()[i][o]));
                        }

                        if (gamelogic.getBoard()[i][o] == '1'){
                            newBtn.setStyle(
                                    "-fx-text-fill: blue;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-background-color: rgba(153,153,153,0.05);"
                            );
                        }
                        if (gamelogic.getBoard()[i][o] == '2'){
                            newBtn.setStyle(
                                    "-fx-text-fill: green;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-background-color: rgba(153,153,153,0.05);"
                            );
                        }
                        if (gamelogic.getBoard()[i][o] == '3'){
                            newBtn.setStyle(
                                    "-fx-text-fill: red;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-background-color: rgba(153,153,153,0.05);"
                            );
                        }
                        if (gamelogic.getBoard()[i][o] == '4'){
                            newBtn.setStyle(
                                    "-fx-text-fill: purple;" +
                                            "-fx-font-weight: bold;" +
                                            "-fx-background-color: rgba(153,153,153,0.05);"
                            );
                        }
                    }
                }

                newBtn.setOnAction(e -> {
                    int newY =GridPane.getColumnIndex((Node) e.getSource());
                    int newX =GridPane.getRowIndex((Node) e.getSource());
                    /* pridanie zobrazeneho policka do listu */
                    int [] cell = {x, y};
                    showed.add(cell);
                    /* update hracej plochy */
                    updateGrid(newGrid, newX, newY);
                });
                newBtn.setPrefWidth(50);
                newBtn.setPrefHeight(50);
                newGrid.add(newBtn, o, i);
            }
        }
        newGrid.setAlignment(Pos.CENTER);
        /* nahradenie hracej plochy novou */
        rootBox.getChildren().remove(0);
        rootBox.getChildren().add(newGrid);
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
