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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.tools.Borders;

import java.io.*;
import java.net.URL;
import java.util.*;

public class GameControler implements Initializable {
    @FXML
    public BorderPane rootBox;
    @FXML
    private Button leaveButton, resetGame;
    private Stage stage;
    private Scene scene;
    private Parent rootP;
    private static final Integer STARTTIME = 0;
    private Timeline timeline;
    @FXML
    private Label counter;
    private Integer timeSeconds = STARTTIME;

    /* velkost hracej plochy */
    private int size, flagsCount, minesCount, showedCount;
    private double x, y;
    private boolean win, play;
    private Logic gamelogic;
    /* list s poliami kde su suradnice  uz zobrazenych policok */
    private LinkedHashSet<int[]> showed = new LinkedHashSet<>();

    @FXML
    private Label flags;
    @FXML
    private Label mines;
    @FXML
    private Label status;

    private BufferedReader br;
    private BufferedWriter bw;
    @FXML
    private Label time;
    private ArrayList<String> times = new ArrayList<>();
    private HashSet<int []> flagsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        status.setText("");
        play = false;
        win = true;
        size = 15;
        flagsCount = 0;
        showedCount = 0;
        gamelogic = new Logic(size);
        minesCount = gamelogic.getMines();
        rootBox.setCenter(getGrid());
        try {
            file();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        time.setText("");
        time();
    }

    public void time(){
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
        mines.setText("Počet mín: "+ gamelogic.getMines());
        flags.setText("Počet vlajok: "+ gamelogic.getFlagsSize());
    }

    public void file() throws IOException {
        File in = new File("src\\currentTime.txt");
        if (!in.exists()){
            in.createNewFile();
        }
        br = new BufferedReader(new FileReader(in));
        bw = new BufferedWriter(new FileWriter("src\\currentTime.txt",false));
        bw.write("");
        bw.close();
    }
    public void getCurrentFile() throws IOException {
        int counter;
        time.setText("");
        String text = "",fullText = "";
        while ((text = br.readLine()) != null){
            times.add(text);
        }
        if (times.size() < 10){
            counter = times.size()-1;
        }
        else counter = 9;
        while(counter >= 0){
            fullText += times.get(counter)+"\n";
            counter--;
        }
        if (times.size() == 10){
            times.remove(0);
        }
        time.setText(fullText);
    }


    public void saveTime(String a){
        try {
            bw = new BufferedWriter(new FileWriter("src\\currentTime.txt",true));
            bw.write(a+"\n");
            bw.close();
        } catch (IOException e) {
            System.out.println("Problem so zapisom");
        }
    }

    public GridPane getGrid(){
        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid");

        for(int i=0;i<size;i++){
            for(int o=0;o<size;o++){
                flagsList = gamelogic.getFlags();
                Button btn = new Button();
                btn.setOnMouseClicked(e -> {
                    int y =GridPane.getColumnIndex((Node) e.getSource());
                    int x =GridPane.getRowIndex((Node) e.getSource());

                    if (e.getButton() == MouseButton.PRIMARY){
                        /* kontrola ci nebola trafena mina */
                        if (gamelogic.getBoard()[x][y] == 'X'){
                            win = false;
                            play = true;
                            getMines();
                            timeline.stop();
                        } else {
                            addToShowed(x, y);
                        }
                        /* update gridu po kliknuti na tlacidlo */

                        updateGrid(grid, x, y, true, false);
                    } else if (e.getButton() == MouseButton.SECONDARY){
                        boolean in = false;
                        for (int [] flag: flagsList){
                            if (flag[0] == x && flag[1] == y){
                                in = true;
                                break;
                            }
                        }

                        if (in){
                            play = gamelogic.removeFlag(x, y);
                            flagsCount --;
                            minesCount ++;
                            flags.setText("Počet vlajok: " + flagsCount);
                            mines.setText("Počet mín: " + minesCount);
                            updateGrid(grid, x, y, false, false);
                        } else if (minesCount != 0){
                            play = gamelogic.addFlag(x, y);
                            flagsCount ++;
                            minesCount --;
                            flags.setText("Počet vlajok: " + flagsCount);
                            mines.setText("Počet mín: " + minesCount);
                            updateGrid(grid, x, y, false, true);
                        }
                    }
                });
                btn.getStyleClass().add("grid-btn");
                grid.add(btn, i, o);
            }
        }
        grid.getStyleClass().add("grid");
        return grid;
    }

    public void updateGrid(GridPane grid, int x, int y, boolean showBtn, boolean flagAdd){
        if (showedCount == ((size * size) - minesCount)) {
            play = true;
            win = true;
            timeline.stop();
        }
        if (play && win){
            status.setStyle("-fx-text-fill: green");
            status.setText("Vyhral si!");
        } else if (play && !win){
            status.setStyle("-fx-text-fill: red");
            status.setText("Prehral si!");
        }
        GridPane newGrid = new GridPane();
        if (showBtn){
            addToShowed(x, y);
        }
        newGrid.getStyleClass().add("grid");
        for (int [] cell: showed){
            System.out.println(Arrays.toString(cell));
        }
        System.out.println("================================");

        for(int i=0;i<size;i++){
            for(int o=0;o<size;o++){
                flagsList = gamelogic.getFlags();
                Button newBtn = showBtn(grid, i, o);


                for (int [] flag: flagsList){
                    if (flag[0] == i && flag[1] == o && newBtn.getText().equals("\uD83D\uDCA3") && play && !win){
                        newBtn = new Button("❌");
                        newBtn.getStyleClass().add("mine-btn");
                    } else if (flag[0] == i && flag[1] == o){
                        newBtn = new Button("\uD83D\uDEA9");
                    }
                }
                String btnText = newBtn.getText();
                /* ak nebola trafna mina tak sa updatne grid aj s action na tlacidlach */

                if (!play && showedCount != ((size * size) - minesCount)) {

                    newBtn.setOnMouseClicked(e -> {
                        int newY =GridPane.getColumnIndex((Node) e.getSource());
                        int newX =GridPane.getRowIndex((Node) e.getSource());


                        if (e.getButton() == MouseButton.PRIMARY && !btnText.equals("\uD83D\uDEA9")){
                            /* kontrola ci nebola stlacena mina */
                            if (gamelogic.getBoard()[newX][newY] == 'X'){
                                win = false;
                                play = true;
                                getMines();
                                timeline.stop();
                            } else {
                                addToShowed(newX, newY);
                            }
                            /* update gridu po stlaceni na tlacidlo */
                            updateGrid(newGrid, newX, newY, true, false);
                        } else if (e.getButton() == MouseButton.SECONDARY){
                            boolean in = false;
                            for (int [] flag: flagsList){
                                if (flag[0] == newX && flag[1] == newY){
                                    in = true;
                                    break;
                                }
                            }

                            if (in){
                                play = gamelogic.removeFlag(newX, newY);
                                flagsCount --;
                                minesCount ++;
                                flags.setText("Počet vlajok: " + flagsCount);
                                mines.setText("Počet mín: " + minesCount);
                                updateGrid(newGrid, newX, newY, false, false);
                            } else if (minesCount != 0){
                                play = gamelogic.addFlag(newX, newY);
                                flagsCount ++;
                                minesCount --;
                                flags.setText("Počet vlajok: " + flagsCount);
                                mines.setText("Počet mín: " + minesCount);
                                updateGrid(newGrid, newX, newY, false, true);
                            }
                        }
                    });
                } else {
                    timeline.stop();
                }
                newBtn.getStyleClass().add("grid-btn");
                newGrid.add(newBtn, o, i);
            }
        }
        newGrid.getStyleClass().add("grid");
        rootBox.setCenter(newGrid);
    }

    public void addToShowed(int x, int y){
        boolean in = false;
        for (int [] cell: gamelogic.getOne(x, y)){
            for (int [] show: showed){
                if (cell[0] == show[0] && cell[1] == show[1]){
                    in = true;
                    break;
                }
            }
            if (!in) showed.add(cell);
        }
        showedCount = showed.size();
    }

    /* zobrazenie obsahu tlacidla po stlaceni */
    public Button showBtn(GridPane grid, int x, int y) {
        Button newBtn = new Button();
        for (int[] cell : showed) {
            if (cell[0] == x && cell[1] == y) {
                if (gamelogic.getBoard()[x][y] == '0') {
                    newBtn = new Button();
                    newBtn.getStyleClass().add("zero-btn");
                } else if (gamelogic.getBoard()[x][y] == 'X' && play && !win) {
                    newBtn = new Button("\uD83D\uDCA3");
                } else if (gamelogic.getBoard()[x][y] != 'X'){
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
                if (gamelogic.getBoard()[x][y] == 'X' && play && !win) {
                    newBtn.getStyleClass().add("mine-btn");
                }
            }
        }
        return newBtn;
    }

    /* ziskanie pozicii min */
    public void getMines(){
        for(int i=0;i<size;i++){
            for(int o=0;o<size;o++){
                if (gamelogic.getBoard()[i][o] == 'X'){
                    int [] cell = {i, o};
                    showed.add(cell);
                }
            }
        }
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
        if(actionEvent.getSource()==resetGame){
            status.setText("");
            showed.clear();
            if (play && win){
                saveTime(String.format("%02d:%02d", (timeSeconds % 3600) / 60, timeSeconds % 60)); // Zmeniť, dať tam kde sa detekuje vyhra
            }
            getCurrentFile();
            win = true;
            play = false;
            size = 15;
            flagsCount = 0;
            showedCount = 0;
            gamelogic = new Logic(size);
            minesCount = gamelogic.getMines();
            rootBox.setCenter(getGrid());
            time();
        }
    }
}