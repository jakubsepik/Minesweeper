package app.minesweeper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
    }

    public GridPane getGrid(){
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
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
                btn.setPrefWidth(50);
                btn.setPrefHeight(50);
                grid.add(btn, i, o);
            }
        }
        return grid;
    }

    public void updateGrid(GridPane grid, int x, int y){
        List<int[]> toShow = gamelogic.getOne(x, y);
        for (int [] in: toShow){
            System.out.println(Arrays.toString(in));
        }


        GridPane newGrid = new GridPane();
        newGrid.setVgap(5);
        newGrid.setHgap(5);
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
                        newBtn = new Button(Character.toString(gamelogic.getBoard()[i][o]));
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
