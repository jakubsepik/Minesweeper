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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.tools.Borders;
import org.w3c.dom.events.Event;

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
    private LinkedHashSet<int[]> showedTest = new LinkedHashSet<>();
    private HashSet<int []> flagsList;

    @FXML
    private Label flags;
    @FXML
    private Label mines;
    @FXML
    private Label status;

    private BufferedReader br;
    private BufferedWriter bw;
    @FXML
    private Label time,score;
    private ArrayList<String> times = new ArrayList<>();
    private ArrayList<String> best = new ArrayList<>();
    final String easy = "src\\leaderEasy.txt",medium = "src\\leaderMedium.txt",hard = "src\\leaderHard.txt";
    private File leader = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        time.setText("");
        score.setText("");
        startGame();
        try {
            file();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // zadefinovanie parametrov na spustenie hry po kazdom resete
    public void startGame(){
        showed.clear();
        showedTest.clear();
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
            getLeader(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        time();
    }

    // obsluha timeru
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

        // vypis poctu min a vlajok
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
    public void getLeader(int f) throws IOException {
        if (f == 1){
            leader = new File(easy);
            if (!leader.exists()) leader.createNewFile();
        }
        if (f == 2){
            leader = new File(medium);
            if (!leader.exists()) leader.createNewFile();
        }
        if (f == 3) {
            leader = new File(hard);
            if (!leader.exists()) leader.createNewFile();
        }

        String text = "",full = "";
        BufferedReader r = new BufferedReader(new FileReader(leader));
        best.clear();
        while ((text = r.readLine()) != null){
            best.add(text);
            full += text+"\n";
            System.out.println("FULL :\n"+full);
        }
        score.setText(full);
    }

    // ulozenie casu po vyhre
    public void saveTime(String a){
        try {
            bw = new BufferedWriter(new FileWriter("src\\currentTime.txt",true));
            bw.write(a+"\n");
            bw.close();
        } catch (IOException e) {
            System.out.println("Problem so zapisom");
        }
    }
    public void saveBestTime(int f,String a) throws IOException{
        BufferedWriter w = new BufferedWriter(new FileWriter(leader,false));
        w.write("");
        best.add(a);

        Collections.sort(best);
        System.out.println(best);

        int counter = 0;
        String fullText = "";
        while(counter < 10 && counter < best.size()){
            fullText += best.get(counter)+"\n";
            w.write(best.get(counter)+"\n");
            counter++;
        }
        score.setText(fullText);
        w.close();
    }

    // kontrola ci sa na tlacitku, na ktore kliknem nachadza vlajka
    public boolean inFlags(int x, int y){
        boolean in = false;
        for (int [] flag: flagsList){
            if (flag[0] == x && flag[1] == y){
                in = true;
                break;
            }
        }
        return in;
    }

    // odstranenie vlajky z tlacitka
    public void removeFlag(GridPane grid, int x, int y){
        play = gamelogic.removeFlag(x, y);
        flagsCount --;
        minesCount ++;
        flags.setText("Počet vlajok: " + flagsCount);
        mines.setText("Počet mín: " + minesCount);
        System.out.println("prešlo odstranenie");
        updateGrid(grid, x, y, false);
    }

    // pridanie vlajky na tlacitka
    public void addFlag(GridPane grid, int x, int y){
        play = gamelogic.addFlag(x, y);
        flagsCount ++;
        minesCount --;
        flags.setText("Počet vlajok: " + flagsCount);
        mines.setText("Počet mín: " + minesCount);
        System.out.println("prešlo pridanie");
        updateGrid(grid, x, y, false);
    }

    // hitnutie miny
    public void mineHit(){
        win = false;
        play = true;
        getMines();
        timeline.stop();
    }

    public boolean canPutFlag(int x, int y){
        boolean possible = true;
        for (int [] cell: showed){
            if (cell[0] == x && cell[1] == y){
                possible = false;
                break;
            }
        }
        for (int [] cell: flagsList){
            if (cell[0] == x && cell[1] == y){
                possible = true;
                break;
            }
        }
        return possible;
    }

    public void checkClick(MouseEvent e, GridPane grid, String btnText){
        int y =GridPane.getColumnIndex((Node) e.getSource());
        int x =GridPane.getRowIndex((Node) e.getSource());
        if (e.getButton() == MouseButton.PRIMARY && !btnText.equals("\uD83D\uDEA9")){ // lavy klik
            System.out.println("prešiel lavy klik");
            if (gamelogic.getBoard()[x][y] == 'X') mineHit(); // hitnutie miny
            else addToShowed(x, y);
            checkIfWin();
            updateGrid(grid, x, y, true);
        } else if (e.getButton() == MouseButton.SECONDARY && canPutFlag(x, y)){ // pravy klik
            System.out.println("prešiel klik");
            if (inFlags(x, y)) removeFlag(grid, x, y);
            else if (minesCount != 0) addFlag(grid, x, y);
            updateGrid(grid, x, y, false);
        }
    }

    // kontrola a vypis vlajky
    public Button checkIfFlag(Button newBtn, int x, int y){
        for (int [] flag: flagsList){
            // emoji x (mina oznacena vlajkou) po prehre
            if (flag[0] == x && flag[1] == y && newBtn.getText().equals("\uD83D\uDCA3") && play && !win){
                newBtn = new Button("❌");
                newBtn.getStyleClass().add("mine-btn");
            } else if (flag[0] == x && flag[1] == y){
                newBtn = new Button("\uD83D\uDEA9");  // emoji vlajky
            }
        }
        return newBtn;
    }

    // kontrola vyhry
    public void checkIfWin(){
        if (showedCount == (size * size) - gamelogic.getMines()){
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
    }

    public boolean inShowed(int x, int y){
        for (int [] cell: showed){
            if (cell[0] == x && cell[1] == y){
                return true;
            }
        }
        return false;
    }

    // vypis suradnic zobrazenych policok
    public void printShowed(LinkedHashSet<int []> set){
        for (int [] cell: set){
            System.out.println(Arrays.toString(cell));
        }
        System.out.println("================================");
    }

    // prvotne ziskanie gridu
    public GridPane getGrid(){
        flagsList = gamelogic.getFlags(); // ziskanie kolekcie so suradnicami vlajok
        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid");

        // generovanie tlacitok
        for(int i=0;i<size;i++){
            for(int o=0;o<size;o++){
                Button btn = new Button();
                String btnText = btn.getText();
                if (!inShowed(i, o)){
                    btn.setOnMouseClicked(e -> {checkClick(e, grid, btnText);});
                }
                btn.getStyleClass().add("grid-btn");
                grid.add(btn, i, o);
            }
        }
        grid.getStyleClass().add("grid");
        return grid;
    }

    // update gridu po stlaceni na policko
    public void updateGrid(GridPane grid, int x, int y, boolean showBtn){
        flagsList = gamelogic.getFlags(); // ziskanie kolekcie so suradnicami vlajok
        checkIfWin();
        GridPane newGrid = new GridPane();
        newGrid.getStyleClass().add("grid");
        for(int i=0;i<size;i++){
            for(int o=0;o<size;o++){
                Button newBtn = showBtn(i, o);
                newBtn = checkIfFlag(newBtn, i ,o); // kontrola a vypis vlajky
                String btnText = newBtn.getText();
                if (play && win && btnText.equals("") && gamelogic.getBoard()[i][o] == 'X'){
                    newBtn = new Button("\uD83D\uDEA9");
                }
                printShowed(showed);
                if (!play && !inShowed(i, o)) {
                    newBtn.setOnMouseClicked(e -> {checkClick(e, newGrid, btnText);});
                } else if (play){
                    timeline.stop();
                };
                newBtn.getStyleClass().add("grid-btn");
                newGrid.add(newBtn, o, i);
            }
        }
        newGrid.getStyleClass().add("grid");
        rootBox.setCenter(newGrid);
    }

    // pridanie suradnic do zobrazenych
    public void addToShowed(int x, int y){
        for (int [] cell: gamelogic.getOne(x, y)){
            boolean in = false;
            for (int [] test: showed){
                if (cell[0] == test[0] && cell[1] == test[1]){
                    in = true;
                }
            }
            if (!in){
                showed.add(new int[]{cell[0], cell[1]});
            }
        }
        showedCount = showed.size();
    }

    // priradenie hodnoty pre policko
    public Button showBtn(int x, int y) {
        Button newBtn = new Button();
        for (int[] cell : showed) {
            if (cell[0] == x && cell[1] == y) {
                if (gamelogic.getBoard()[x][y] == '0') {
                    newBtn = new Button(" ");
                    newBtn.getStyleClass().add("zero-btn");
                } else if (gamelogic.getBoard()[x][y] == 'X') newBtn = new Button("\uD83D\uDCA3");
                else newBtn = new Button(Character.toString(gamelogic.getBoard()[x][y]));

                if (gamelogic.getBoard()[x][y] == '1') newBtn.getStyleClass().add("one-btn");
                if (gamelogic.getBoard()[x][y] == '2') newBtn.getStyleClass().add("two-btn");
                if (gamelogic.getBoard()[x][y] == '3') newBtn.getStyleClass().add("three-btn");
                if (gamelogic.getBoard()[x][y] == '4') newBtn.getStyleClass().add("four-btn");
                if (gamelogic.getBoard()[x][y] == '5') newBtn.getStyleClass().add("five-btn");
                if (gamelogic.getBoard()[x][y] == '6') newBtn.getStyleClass().add("six-btn");
                if (gamelogic.getBoard()[x][y] == '7') newBtn.getStyleClass().add("seven-btn");
                if (gamelogic.getBoard()[x][y] == '8') newBtn.getStyleClass().add("eight-btn");
                if (gamelogic.getBoard()[x][y] == 'X') newBtn.getStyleClass().add("mine-btn");
                break;
            }
        }
        return newBtn;
    }

    // ziskanie suradnic s minami
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
        if(actionEvent.getSource()==leaveButton){ // opustenie hry
            Parent rootP = FXMLLoader.load(getClass().getResource("view.fxml"));
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(rootP);
            scene.getStylesheets().add(getClass().getResource("style.css").toString());
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        }
        if(actionEvent.getSource()==resetGame){ // resetovanie hry
            if (play && win){
                System.out.println("som gay");
                saveTime(String.format("%02d:%02d", (timeSeconds % 3600) / 60, timeSeconds % 60)); // Zmeniť, dať tam kde sa detekuje vyhra
                saveBestTime(1,String.format("%02d:%02d", (timeSeconds % 3600) / 60, timeSeconds % 60));
            }
            getCurrentFile();
            startGame();
        }
    }
}