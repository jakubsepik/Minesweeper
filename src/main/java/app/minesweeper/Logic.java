package app.minesweeper;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Logic {
    private int size;
    private int mines;
    private final ArrayList<int[]> flags;
    private final char[][] board;
    Random rn = new Random();
    public Logic(int size) {
        this.size = size;
        this.flags = new ArrayList<>();
        board = new char[size][size];
        for(int i=0;i<size;i++){
            char[] tmp = new char[size];
            Arrays.fill(tmp, (char) 48);
            board[i]=tmp;
        }

        mines = size*size/5;

        for (byte i = 0; i < mines; i++) {
            int x = rn.nextInt(size);
            int y = rn.nextInt(size);
            if (board[x][y]!='X') {
                board[x][y] = 'X';

                if (x + 1 < board.length)
                    if (board[x + 1][y] != 88)
                        board[x + 1][y]++;

                if (y + 1 < board.length)
                    if (board[x][y + 1] != 88)
                        board[x][y + 1]++;

                if (x + 1 < board.length && y + 1 < board.length)
                    if (board[x + 1][y + 1] != 88)
                        board[x + 1][y + 1]++;

                if (x - 1 >= 0 && y - 1 >= 0)
                    if (board[x - 1][y - 1] != 88)
                        board[x - 1][y - 1]++;

                if (x - 1 >= 0)
                    if (board[x - 1][y] != 88)
                        board[x - 1][y]++;

                if (y - 1 >= 0)
                    if (board[x][y - 1] != 88)
                        board[x][y - 1]++;

                if (x + 1 < board.length && y - 1 >= 0)
                    if (board[x + 1][y - 1] != 88)
                        board[x + 1][y - 1]++;

                if (x - 1 >= 0 && y + 1 < board.length)
                    if (board[x - 1][y + 1] != 88)
                        board[x - 1][y + 1]++;
            }else
                i--;
        }
    }
    public char getOne(int x,int y){
        return board[x][y];
    }
    public boolean addFlag(int x,int y){
        flags.add(new int[]{x,y});
        return checkWin();
    }
    public boolean removeFlag(int x,int y){
        flags.remove(new int[]{x,y});
        return checkWin();
    }
    private boolean checkWin(){
        if(flags.size()!=mines)
            return false;
        for (int[] flag : flags) {
            if (board[flag[0]][flag[1]] != 'X')
                return false;
        }
        return true;
    }
    public char[][] getBoard() {
        return board;
    }
    public GridPane getGrid(){
        GridPane grid = new GridPane();
        for(int i=0;i<size;i++){
            for(int o=0;o<size;o++){
                Button btn = new Button(Character.toString(board[i][o]));
                btn.setOnAction(e -> {
                    int x =GridPane.getColumnIndex((Node) e.getSource());
                    int y =GridPane.getRowIndex((Node) e.getSource());
                    System.out.println(x + " " + y);
                });
                grid.add(btn, i, o);
            }
        }
        return grid;
    }
}
