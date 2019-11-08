import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

//import javax.sound.sampled.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static int value = 1;
    public static int pointsToWin = 3;
    public static final int TOPOFPlAYINGFIELD=1;

    public static int speed = 90;
    public static final TextColor WHITE = new TextColor.RGB(255, 255, 255);
    public static final TextColor PLAYERONE_COLOR = new TextColor.RGB(255, 0, 0); //make the player one mask red
    public static final TextColor PLAYERTWO_COLOR = new TextColor.RGB(0, 255, 0); //make the player two mask green
    public static final TextColor BOTMASK_COLOR = new TextColor.RGB(0, 0, 255); //make the botmask blue
    public static ArrayList<Mask> maskar;
    public static boolean twoPlayers;



    public static void main(String[] args) throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException, LineUnavailableException, UnsupportedAudioFileException {


        //initialize terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);

        //initialize screen and ask for 1 or 2 players
        twoPlayers = true;
        boolean includeBot = true;
        Screen screen = new Screen(terminal); //initialize new screen object
        twoPlayers = screen.startScreen(terminal); //method call to start screen

        //Initialize new screen to ask for user input about game difficulty. Return speed value. 
        speed = screen.selectDifficulty(terminal);

        //display initial score for player one
        terminal.setForegroundColor(PLAYERONE_COLOR);
        terminal.setCursorPosition(terminal.getTerminalSize().getColumns()-14, 0);
        String points = "Points: " + 0;
        for(char c : points.toCharArray()){
            terminal.putCharacter(c);
        }

        //display initial score for player two (if there is one)
        if(twoPlayers) {
            terminal.setForegroundColor(PLAYERTWO_COLOR);
            terminal.setCursorPosition(1, 0);
            String points2 = "Points: " + 0;
            for (char c : points2.toCharArray()) {
                terminal.putCharacter(c);
            }
        }

        //variables used in gameloop
        KeyStroke keyStroke = null;
        KeyType type = null;
        char keyStrokeChar = ' ';
        boolean continuePlaying = true;


        //initialize walls for level 1
        Obstacles wallsLevel1 = new Obstacles(terminal);

        //initialize mask player1
        Position position1 = new Position(50, 15);
        Mask mask1 = new Mask(position1, speed, PLAYERONE_COLOR);
        mask1.printMask(terminal);

        maskar = new ArrayList<>();
        maskar.add(mask1);

        //initialize mask player2

        Position position2 = new Position(10, 10);
        Mask mask2 = new Mask(position2, speed, PLAYERTWO_COLOR);
        if(twoPlayers){
         mask2.printMask(terminal);
         maskar.add(mask2);
        }

        //initialize botMask
        Position position3 = new Position(30, 5);
        BotMask botMask = new BotMask(position3, speed, BOTMASK_COLOR);
        if(includeBot){
            botMask.printMask(terminal);
            maskar.add(botMask);
        }

        //generate random position without collisions for first number to catch
        generateNewNumber(1, wallsLevel1, terminal);

        //gameplay loop
        while (continuePlaying) {

            //check for keystroke from user(s)
            keyStroke = terminal.pollInput();
            if (keyStroke != null) {
                KeyType keyType = keyStroke.getKeyType();
                if (keyStroke.getCharacter() != null) {
                    keyStrokeChar = keyStroke.getCharacter();
                    System.out.println("character: " + keyStrokeChar);
                    //Player two uses characters to change direction of the mask
                    if(twoPlayers){
                        mask2.changeDirectionPlayerTwo(keyStrokeChar);
                    }
                } else {
                    //Player one uses arrow keys to change direction of the mask
                    mask1.changeDirectionPlayerOne(keyType);
                }
            }



            if(twoPlayers){
                continuePlaying=mask1.moveMaskForward(terminal, wallsLevel1, maskar)
                        && mask2.moveMaskForward(terminal, wallsLevel1, maskar) ;
            }
            else
            {
                continuePlaying = mask1.moveMaskForward(terminal, wallsLevel1, maskar);
            }

            //handle movement of the botMask
            if(includeBot){
                botMask.setDirection();
                botMask.moveMaskForward(terminal, wallsLevel1, maskar);
            }

            //check if the player wants to quit the game
            if (keyStrokeChar == Character.valueOf('q')) {
                continuePlaying = false;
                System.out.println("quit");
                terminal.close();
            }

            //check if player wants to pause game
//            else if (keyStrokeChar == Character.valueOf('p')){
//                System.out.println("Pausing game. Press p to resume");
//                while(true){
//                    keyStroke = terminal.pollInput();
//                    if (keyStroke!=null && keyStroke.getCharacter() != null) {
//
//                        keyStrokeChar = keyStroke.getCharacter();
//                        if(keyStrokeChar== Character.valueOf('p'));
//                    }
//                }
//            }
        }
        //Show end game screen
        screen.endScreen(terminal);

        terminal.close();
    }

    //method to generate new number at a position that won't collide with any obstacle or mask
    public static void generateNewNumber(int value, Obstacles obstacles, Terminal terminal) throws IOException {

        Position numberPosition = null;
        boolean positionOk = false;
        //loop until generated position is ok
        while(!positionOk) {
            positionOk = true;
            numberPosition = new Position(ThreadLocalRandom.current().nextInt(0, terminal.getTerminalSize().getColumns()-1), ThreadLocalRandom.current().nextInt(TOPOFPlAYINGFIELD, terminal.getTerminalSize().getRows()-1));



            //make sure new number position doesn't collide with any walls or other obstacles
            for(Position pos : obstacles.getObstacles()) {
                if (numberPosition.x == pos.x  && numberPosition.y == pos.y){
                    positionOk = false;
                    break;
                }
            }

            //make sure the new number position doesn't collide with any mask
            for(Mask mask: maskar){
                for(Position pos : mask.getMaskPositions()) {
                    if (numberPosition.x == pos.x  && numberPosition.y == pos.y){
                        positionOk = false;
                        break;
                    }
                }
            }
        }

        //store the generated position
        for(Mask mask: maskar) {
            mask.setNumberPosition(numberPosition);
        }

        // print number to screen
        terminal.setForegroundColor(WHITE);
        terminal.setCursorPosition(numberPosition.x, numberPosition.y);
        terminal.putCharacter((char)(value + '0'));
        terminal.flush();
    }


    //method to update the score(s) shown at the top of the terminal. This method is called from the Mask class
    public static void updateScore(Terminal terminal) throws IOException {

        //update score for player one
        char[] scoreAsCharArray = ("" + maskar.get(0).getScore()).toCharArray();
        //set cursor at top left corner with enough room left to print out the score
        terminal.setCursorPosition(terminal.getTerminalSize().getColumns()-6, 0);
        terminal.setForegroundColor(Main.PLAYERONE_COLOR);
        for(char c: scoreAsCharArray){
            terminal.putCharacter(c);
        }

        //update score for player two (if we have two players)
        if(Main.twoPlayers){
            char[] scoreAsCharArray2 = ("" + maskar.get(1).getScore()).toCharArray();
            //set cursor at top left corner, after "Points:"
            terminal.setCursorPosition(9, 0);
            terminal.setForegroundColor(Main.PLAYERTWO_COLOR);
            for(char c: scoreAsCharArray2){
                terminal.putCharacter(c);
            }
        }
    }






}
