package app.minesweeper;

import java.util.Random;
import java.util.Scanner;

public class OldLogic {
    public static void main(String[] args) {
        Random rn = new Random();
        Scanner sc = new Scanner(System.in);
        boolean prehra=false;
        int pocet=0,pocetMinH=0,pocetMin=0;
        char rad1[] = {48, 48, 48, 48, 48, 48, 48, 48, 48};
        char rad2[] = {48, 48, 48, 48, 48, 48, 48, 48, 48};
        char rad3[] = {48, 48, 48, 48, 48, 48, 48, 48, 48};
        char rad4[] = {48, 48, 48, 48, 48, 48, 48, 48, 48};
        char rad5[] = {48, 48, 48, 48, 48, 48, 48, 48, 48};
        char rad6[] = {48, 48, 48, 48, 48, 48, 48, 48, 48};
        char rad7[] = {48, 48, 48, 48, 48, 48, 48, 48, 48};
        char rad8[] = {48, 48, 48, 48, 48, 48, 48, 48, 48};
        char rad9[] = {48, 48, 48, 48, 48, 48, 48, 48, 48};
        char[][] stlpce = {rad1, rad2, rad3, rad4, rad5, rad6,rad7, rad8, rad9 };


        char Zrad1[] = {'#', '#', '#', '#', '#', '#', '#', '#', '#'};
        char Zrad2[] = {'#', '#', '#', '#', '#', '#', '#', '#', '#'};
        char Zrad3[] = {'#', '#', '#', '#', '#', '#', '#', '#', '#'};
        char Zrad4[] = {'#', '#', '#', '#', '#', '#', '#', '#', '#'};
        char Zrad5[] = {'#', '#', '#', '#', '#', '#', '#', '#', '#'};
        char Zrad6[] = {'#', '#', '#', '#', '#', '#', '#', '#', '#'};
        char Zrad7[] = {'#', '#', '#', '#', '#', '#', '#', '#', '#'};
        char Zrad8[] = {'#', '#', '#', '#', '#', '#', '#', '#', '#'};
        char Zrad9[] = {'#', '#', '#', '#', '#', '#', '#', '#', '#'};
        char[][] Zstlpce = {Zrad1, Zrad2, Zrad3, Zrad4, Zrad5, Zrad6,Zrad7, Zrad8,Zrad9 };

        System.out.println("hra miny");
        System.out.println("len pri oznaceni vsetkych min je vyhra");
        System.out.println("hra sa na ploche pomocou x y suradnic");
        System.out.println("y┃ # # #\n" +
                " ┃ # # #\n" +
                " ┃ # # #\n" +
                " ╋━━━━━ x\n");


        for (byte i = 0; i < Math.pow(stlpce.length, 2)/10; i++) {
            int x = rn.nextInt(stlpce.length);
            int y = rn.nextInt(stlpce.length);
            if (stlpce[x][y]!='X') {
                stlpce[x][y] = 'X';

                if (x + 1 < stlpce.length)
                    if (stlpce[x + 1][y] != 88)
                        stlpce[x + 1][y]++;

                if (y + 1 < stlpce.length)
                    if (stlpce[x][y + 1] != 88)
                        stlpce[x][y + 1]++;

                if (x + 1 < stlpce.length && y + 1 < stlpce.length)
                    if (stlpce[x + 1][y + 1] != 88)
                        stlpce[x + 1][y + 1]++;

                if (x - 1 >= 0 && y - 1 >= 0)
                    if (stlpce[x - 1][y - 1] != 88)
                        stlpce[x - 1][y - 1]++;

                if (x - 1 >= 0)
                    if (stlpce[x - 1][y] != 88)
                        stlpce[x - 1][y]++;

                if (y - 1 >= 0)
                    if (stlpce[x][y - 1] != 88)
                        stlpce[x][y - 1]++;

                if (x + 1 < stlpce.length && y - 1 >= 0)
                    if (stlpce[x + 1][y - 1] != 88)
                        stlpce[x + 1][y - 1]++;

                if (x - 1 >= 0 && y + 1 < stlpce.length)
                    if (stlpce[x - 1][y + 1] != 88)
                        stlpce[x - 1][y + 1]++;

                pocetMin++;
            }else
                i--;
        }
        System.out.println("-------------------------------------------------------------------");

        for (byte i = 0; i < Zstlpce.length; i++) {
            for (byte o = 0; o < Zstlpce[i].length; o++)
                System.out.print(Zstlpce[i][o]+ "  ");
            System.out.println();
        }

        do {
            pocetMinH=0;
            System.out.println("zadaj 'm' ak chceš oznacit minu\n"+"inak zadaj 'n' ak chces oznacit volne pole");
            System.out.println("hneď za tým zadaj x y suradnice napr 'm12' " );
            String volba = sc.nextLine();
            char volbaV = volba.charAt(0);
            if (volbaV != 'n' && volbaV !='m'){
                System.out.println("zle pismeno");
                continue;
            }
            int xh = Integer.parseInt(String.valueOf(volba.charAt(1)))-1;
            if (xh>=stlpce.length || xh<0){
                System.out.println("zla suradnica");
                continue;
            }
            int yh = stlpce.length-1-(Integer.parseInt(String.valueOf(volba.charAt(2)))-1);
            if (yh>=stlpce.length || yh<0){
                System.out.println("zla suradnica");
                continue;
            }
            if (volbaV=='m'){
                Zstlpce[yh][xh] = '$';
            }else{
                if (stlpce[yh][xh] == 88) {
                    Zstlpce[yh][xh] = stlpce[yh][xh];
                    prehra = true;
                    break;
                }
                Zstlpce[yh][xh] = stlpce[yh][xh];
            }
            do {
                pocet = 0;pocetMinH=0;
                for (byte i = 0; i < stlpce.length; i++) {
                    for (byte o = 0; o < Zstlpce[i].length; o++) {

                        if (Zstlpce[i][o]=='$'&& stlpce[i][o]=='X')
                            pocetMinH++;


                        if (Zstlpce[i][o] != '#' && stlpce[i][o] == 48) {
                            if (i + 1 < stlpce.length && Zstlpce[i + 1][o] == '#') {
                                Zstlpce[i + 1][o] = stlpce[i + 1][o];
                                pocet++;
                            }

                            if (i - 1 >= 0 && Zstlpce[i - 1][o] == '#') {
                                Zstlpce[i - 1][o] = stlpce[i - 1][o];
                                pocet++;
                            }

                            if (o + 1 < stlpce.length && Zstlpce[i][o + 1] == '#') {
                                Zstlpce[i][o + 1] = stlpce[i][o + 1];
                                pocet++;
                            }

                            if (o - 1 >= 0 && Zstlpce[i][o - 1] == '#') {
                                Zstlpce[i][o - 1] = stlpce[i][o - 1];
                                pocet++;
                            }
                        }

                    }
                }
            }while  (pocet != 0);

            System.out.println("-------------------------------------------------------------------");



            for (byte i = 0; i < Zstlpce.length; i++) {
                for (byte o = 0; o < Zstlpce[i].length; o++)
                    System.out.print(Zstlpce[i][o]+ "  ");
                System.out.println();
            }

        } while (pocetMin!=pocetMinH);

        if (prehra==true)
            System.out.println("KABOOM");
        else
            System.out.println("vyhral si");

        System.out.println("-------------------------------------------------------------------");
        for (byte i = 0; i < stlpce.length; i++) {
            for (byte o = 0; o < stlpce[i].length; o++){
                System.out.print(Zstlpce[i][o]+"  ");
            }
            System.out.print("                     ");
            for (byte o = 0; o < stlpce[i].length; o++){
                System.out.print(stlpce[i][o]+"  ");
            }
            System.out.println();
        }
    }
}


