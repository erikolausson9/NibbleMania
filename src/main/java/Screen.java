import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Screen {

        //instance variables
        private int terminalColumns;
        private int terminalRows;

        //constructor
        public Screen(Terminal terminal) throws IOException, InterruptedException {

            this.terminalColumns = terminal.getTerminalSize().getColumns();
            this.terminalRows = terminal.getTerminalSize().getRows();
        }

        public boolean startScreen(Terminal terminal) throws IOException, InterruptedException {
            String row1 = "Welcome to Nibble Mania!";
            int row1Length = row1.toCharArray().length;

            terminal.setCursorPosition((terminalColumns/2 - row1Length/2), terminalRows-15);
            for (char c : row1.toCharArray()) {
                terminal.putCharacter(c);
            }

            String row2 = "Press 1 or 2 to start a new game with one or two players";
            int row2Length = row2.toCharArray().length;
            terminal.setCursorPosition((terminalColumns/2 - row2Length/2),terminalRows-12);
            for (char c : row2.toCharArray()) {
                terminal.putCharacter(c);
            }

            String row3 = "or press Q to exit.";
            int row3Length = row3.toCharArray().length;
            terminal.setCursorPosition((terminalColumns/2 - row3Length/2),terminalRows-11);
            for (char c : row3.toCharArray()) {
                terminal.putCharacter(c);
            }

            KeyStroke keyStroke = null;

            while(true){

                do{
                    Thread.sleep(5);
                    keyStroke = terminal.pollInput();
                } while (keyStroke == null);

                KeyType type = keyStroke.getKeyType();
                Character c = keyStroke.getCharacter();
                System.out.println(c);
                System.out.println(type);


                if(c == '1'){
                    terminal.clearScreen();
                    return false;
                }

                else if(c == '2'){
                    terminal.clearScreen();
                    return true;
                }

                else if(c == 'q'){
                    System.out.println("quit");
                    terminal.close();
                }
            }
        }

    public boolean botOrHumanOpponent(Terminal terminal) throws IOException, InterruptedException {

        String row1 = "Choose your opponent:";
        int row1Length = row1.toCharArray().length;

        terminal.setCursorPosition((terminalColumns / 2 - row1Length / 2), terminalRows - 14);
        for (char c : row1.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row2 = "Press 1 for a human opponent";
        int row2Length = row2.toCharArray().length;

        terminal.setCursorPosition((terminalColumns / 2 - row2Length / 2), terminalRows - 12);
        for (char c : row2.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row3 = "Press 2 for a furious predator bot";
        int row3Length = row3.toCharArray().length;

        terminal.setCursorPosition((terminalColumns / 2 - row3Length / 2), terminalRows - 11);
        for (char c : row3.toCharArray()) {
            terminal.putCharacter(c);
        }

        terminal.flush();

        boolean includeBot = false;
        KeyStroke keyStroke = null;

        while (true) {

            do {
                Thread.sleep(5);
                keyStroke = terminal.pollInput();

            } while (keyStroke == null);

            KeyType type = keyStroke.getKeyType();
            Character c = keyStroke.getCharacter();
            System.out.println(c);
            System.out.println(type);

            if (c == '1') {
                includeBot = false;
                break;
            } else if (c == '2') {
                includeBot = true;
                break;
            } else {
                continue;
            }
        }
        terminal.clearScreen();
        return includeBot;
    }


    public int selectDifficulty(Terminal terminal) throws IOException, InterruptedException {

        String row1 = "Select difficulty: 1, 2 or 3 (3 is harder).";
        int row1Length = row1.toCharArray().length;

        terminal.setCursorPosition((terminalColumns/2 - row1Length/2), terminalRows-12);
        for (char c : row1.toCharArray()) {
            terminal.putCharacter(c);
        }

        terminal.flush();

        KeyStroke keyStroke = null;
        int speed = 200;

        while(true){

            do{
                Thread.sleep(5);
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);

            KeyType type = keyStroke.getKeyType();
            Character c = keyStroke.getCharacter();
            System.out.println(c);
            System.out.println(type);

            switch (c){
                case '1':
                    speed = 200;
                    break;
                case '2':
                    speed = 100;
                    break;
                case '3':
                    speed = 50;
                    break;
                default:
                    continue;

            }

            terminal.clearScreen();
            break;
        }
        return speed;
    }


    public void endScreen(Terminal terminal) throws IOException, InterruptedException {

            if(Main.value == Main.pointsToWin+1){
                String row1 = "You won the game!";
                int row1Length = row1.toCharArray().length;

                terminal.setCursorPosition((terminalColumns/2 - row1Length/2), terminalRows-15);
                for (char c : row1.toCharArray()) {
                    terminal.putCharacter(c);
                }
            }

            else{
                String row1 = "Game over!";
                int row1Length = row1.toCharArray().length;

                terminal.setCursorPosition((terminalColumns/2 - row1Length/2), terminalRows-15);
                for (char c : row1.toCharArray()) {
                    terminal.putCharacter(c);
                }

            }


        String row2 = "Press Q to Quit game";
        int row2Length = row2.toCharArray().length;
        terminal.setCursorPosition((terminalColumns/2 - row2Length/2),terminalRows-12);
        for (char c : row2.toCharArray()) {
            terminal.putCharacter(c);
        }

        terminal.flush();

        KeyStroke keyStroke = null;

        while(true){

            do{
                Thread.sleep(5);
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);

            KeyType type = keyStroke.getKeyType();
            Character c = keyStroke.getCharacter();
            System.out.println(c);
            System.out.println(type);

            if(c == 'q'){
                System.out.println("quit");
                terminal.close();
                }

            }
        }


}

