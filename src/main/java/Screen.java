import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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

        //Start screen
        public boolean startScreen(Terminal terminal) throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException {


        //Print stuff to screen
            String row1 = "Welcome to Nibble Mania!";
            int row1Length = row1.toCharArray().length;

            //To center text: split num of columns by 2 and length on string to be printed.
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

            terminal.flush();

            //play intro sound
            String intro = "NewHopeIntro.wav";
            SoundPlayer soundPlayer2 = new SoundPlayer();
            soundPlayer2.playSound(intro);

            //Check for user input (number of players)
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
//Return true for 2 players and false for only 1 player.
                if(c!=null) {
                    if (c == '1') {
                        terminal.clearScreen();
                        Main.twoPlayers = false;
                        break;
                    } else if (c == '2') {
                        terminal.clearScreen();
                        Main.twoPlayers = true;
                        break;
                    } else if (c == 'q') {
                        System.out.println("quit");
                        terminal.close();
                        return false;
                    }
                }
            }
            return true;
        }

        //Screen asking if AI-bot.
    public boolean botOrHumanOpponent(Terminal terminal) throws IOException, InterruptedException {

        String row1 = "Would you like to include a furious AI-controlled predator worm?";
        int row1Length = row1.toCharArray().length;

        terminal.setCursorPosition((terminalColumns / 2 - row1Length / 2), terminalRows - 14);
        for (char c : row1.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row2 = "Yes/No?";
        int row2Length = row2.toCharArray().length;

        terminal.setCursorPosition((terminalColumns / 2 - row2Length / 2), terminalRows - 12);
        for (char c : row2.toCharArray()) {
            terminal.putCharacter(c);
        }

        terminal.flush();

        boolean includeBot = false;
        KeyStroke keyStroke = null;

        //Check for user input
        while (true) {

            do {
                Thread.sleep(5);
                keyStroke = terminal.pollInput();

            } while (keyStroke == null);

            KeyType type = keyStroke.getKeyType();
            Character c = keyStroke.getCharacter();
            System.out.println("rad 127" + c);
            System.out.println(type);

            //set variable includeBot to true or false
            if (c!=null && c == 'n') {
                includeBot = false;
                break;
            } else if (c != null && c == 'y') {
                includeBot = true;
                break;
            } else {
                continue;
            }
        }
        terminal.clearScreen();
        return includeBot;
    }

    //Screen for selecting difficulty
    public int selectDifficulty(Terminal terminal) throws IOException, InterruptedException {

        String row1 = "Select difficulty: 1, 2 or 3 (3 is harder).";
        int row1Length = row1.toCharArray().length;

        terminal.setCursorPosition((terminalColumns/2 - row1Length/2), terminalRows-12);
        for (char c : row1.toCharArray()) {
            terminal.putCharacter(c);
        }

        terminal.flush();

        //User input
        KeyStroke keyStroke = null;
        int speed = 200;

        while(true){

            do{
                Thread.sleep(5);
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);

            KeyType type = keyStroke.getKeyType();
            Character c = keyStroke.getCharacter();
            System.out.println("rad 171" + c);
            System.out.println(type);

            //Returns different values for speed (= time in milliseconds for process to sleep)
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

    //Print instructions
    public void instructionsScreen(Terminal terminal) throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException {

        String row1 = "Instructions";
        int row1Length = row1.toCharArray().length;

        terminal.setCursorPosition((terminalColumns/2 - row1Length/2), terminalRows-18);
        for (char c : row1.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row2 = "Player 1:";
        int row2Length = row2.toCharArray().length;
        terminal.setCursorPosition((terminalColumns/3 - row2Length/2),terminalRows-12);
        for (char c : row2.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row3 = "UP: ArrowUp";
        int row3Length = row3.toCharArray().length;
        terminal.setCursorPosition((terminalColumns/3 - row3Length/2),terminalRows-10);
        for (char c : row3.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row4 = "Down: ArrowDown";
        int row4Length = row4.toCharArray().length;
        terminal.setCursorPosition((terminalColumns/3 - row4Length/2),terminalRows-9);
        for (char c : row4.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row5 = "Left: ArrowLeft";
        int row5Length = row5.toCharArray().length;
        terminal.setCursorPosition((terminalColumns/3 - row5Length/2),terminalRows-8);
        for (char c : row5.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row6 = "Right: ArrowRight";
        int row6Length = row6.toCharArray().length;
        terminal.setCursorPosition((terminalColumns/3 - row6Length/2),terminalRows-7);
        for (char c : row6.toCharArray()) {
            terminal.putCharacter(c);
        }

        //Instructions for player 2:
        String row7 = "Player 2:";
        int row7Length = row2.toCharArray().length;
        terminal.setCursorPosition((2*terminalColumns/3 - row7Length/2),terminalRows-12);
        for (char c : row7.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row8 = "UP: W";
        int row8Length = row8.toCharArray().length;
        terminal.setCursorPosition((2*terminalColumns/3 - row8Length/2),terminalRows-10);
        for (char c : row8.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row9 = "Down: S";
        int row9Length = row9.toCharArray().length;
        terminal.setCursorPosition((2*terminalColumns/3 - row9Length/2),terminalRows-9);
        for (char c : row9.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row10 = "Left: A";
        int row10Length = row10.toCharArray().length;
        terminal.setCursorPosition((2*terminalColumns/3 - row10Length/2),terminalRows-8);
        for (char c : row10.toCharArray()) {
            terminal.putCharacter(c);
        }

        String row11 = "Right: D";
        int row11Length = row11.toCharArray().length;
        terminal.setCursorPosition((2*terminalColumns/3 - row11Length/2),terminalRows-7);
        for (char c : row11.toCharArray()) {
            terminal.putCharacter(c);
        }

        //How to start game
        String row12 = "Press Enter to start";
        int row12Length = row12.toCharArray().length;
        terminal.setCursorPosition((terminalColumns/2 - row12Length/2),terminalRows-5);
        for (char c : row12.toCharArray()) {
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
            System.out.println("rad 299" + c);
            System.out.println(type);

            if(type == KeyType.Enter){
                terminal.clearScreen();

                break;
            }

        }

    }



    public void endScreen(Terminal terminal, boolean playersWin) throws IOException, InterruptedException {

            terminal.setForegroundColor(Main.WHITE);

            if(playersWin){
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
            //System.out.println(" trying to quit" + c);
            //System.out.println(type);

            if(c !=null && c == 'q'){
                System.out.println("quit");
                terminal.close();
                break;
                }

            }
        }

}

