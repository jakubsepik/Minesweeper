package app.minesweeper;

import java.util.Arrays;
import java.util.Random;

public class Logic {
    private int size;
    private int mines;
    private char[][] board;
    Random rn = new Random();
    public Logic(int size) {
        this.size = size;
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

    public char[][] getBoard() {
        return board;
    }
}
