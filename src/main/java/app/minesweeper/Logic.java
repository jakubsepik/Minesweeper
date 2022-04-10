package app.minesweeper;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import app.minesweeper.GameControler;

import java.util.*;

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
        for(char[] tmp:board){
            System.out.println(Arrays.toString(tmp));
        }
    }
    public List<int[]> getOne(int x,int y){
        List<int[]> list = new ArrayList<>();
        list.add(new int[]{x,y,board[x][y]});
        if(board[x][y]!='0')
            return list;
        Queue<int[]> zeros = new LinkedList<>();
        zeros.add(new int[]{x,y});

        boolean zmena;
        do{
            zmena=false;
            int zero_size = zeros.size();
            for(int i=0;i<zero_size;i++){
                int[] tmp = zeros.remove();
                int xl = tmp[0];
                int yl = tmp[1];

                if (xl + 1 < size && !containsXY(list,xl+1,yl)) {
                    list.add(new int[]{xl+1,yl,board[xl+1][yl]});
                    if(board[xl+1][yl]=='0'){
                        zeros.add(new int[]{xl+1,yl});
                        zmena=true;
                    }
                }

                if (xl - 1 >= 0 && !containsXY(list,xl-1,yl)) {
                    list.add(new int[]{xl-1,yl,board[xl-1][yl]});
                    if(board[xl-1][yl]=='0'){
                        zeros.add(new int[]{xl-1,yl});
                        zmena=true;
                    }
                }

                if (yl + 1 < size && !containsXY(list,xl,yl+1)) {
                    list.add(new int[]{xl,yl+1,board[xl][yl+1]});
                    if(board[xl][yl+1]=='0'){
                        zeros.add(new int[]{xl,yl+1});
                        zmena=true;
                    }
                }

                if (yl - 1 >= 0 && !containsXY(list,xl,yl-1)) {
                    list.add(new int[]{xl,yl-1,board[xl][yl-1]});
                    if(board[xl][yl-1]=='0'){
                        zeros.add(new int[]{xl,yl-1});
                        zmena=true;
                    }
                }

            }
        }while(zmena);
        zeros.clear();
        return list;
    }

    private boolean containsXY(List<int[]> list,int x, int y){
        for(int[] tmp:list){
            if(tmp[0]==x && tmp[1]==y)
                return true;
        }
        return false;
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
}
