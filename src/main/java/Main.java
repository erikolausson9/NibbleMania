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
    public static int pointsToWin = 2;
    public static final int TOPOFPlAYINGFIELD=1;
    public static int score = 0;
    public static int speed = 200;
    public static final TextColor WHITE = new TextColor.RGB(255, 255, 255);
    public static final TextColor PLAYERONE = new TextColor.RGB(255, 0, 0);
    public static final TextColor PLAYERTWO = new TextColor.RGB(0, 255, 0);
    public static ArrayList<Mask> maskar;

    public static void main(String[] args) throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException, LineUnavailableException, UnsupportedAudioFileException {


        boolean twoPlayers = true;

        //initialize terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();


        terminal.setCursorVisible(false);

        //initialize screen
        Screen screen = new Screen(terminal);

        twoPlayers = screen.startScreen(terminal);

        //display score
        terminal.setForegroundColor(WHITE);
        terminal.setCursorPosition(terminal.getTerminalSize().getColumns()-14, 0);
        String points = "Points: ";
        points += Integer.toString(score);
        for(char c : points.toCharArray()){
            terminal.putCharacter(c);
        }




        KeyStroke keyStroke = null;
        KeyType type = null;
        char keyStrokeChar = ' ';
        boolean continuePlaying = true;



        //initialize walls for level 1
        Obstacles wallsLevel1 = new Obstacles(terminal);

        //initialize mask player1
        Position position1 = new Position(50, 15);
        Mask mask1 = new Mask(position1, speed, PLAYERONE);
        mask1.printMask(terminal);

        maskar = new ArrayList<>();
        maskar.add(mask1);

        //initialize mask player2
        Position position2 = new Position(10, 10);
        BotMask mask2 = new BotMask(position2, speed, PLAYERTWO);
        if(twoPlayers){
         mask2.printMask(terminal);
         maskar.add(mask2);
        }



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
                    //call the Mask class and see if the direction should change

                    if(twoPlayers){
                        //mask2.changeDirectionPlayerTwo(keyStrokeChar);
                    }

                } else {
                    System.out.println("type: " + keyType);
                    //call the Mask class and see if the direction should change
                    mask1.changeDirectionPlayerOne(keyType);
                }
            }

            mask2.setDirection();

            if(twoPlayers){
                continuePlaying=mask1.moveMaskForward(terminal, wallsLevel1, maskar)
                        && mask2.moveMaskForward(terminal, wallsLevel1, maskar) ;
            }
            else
            {
                continuePlaying = mask1.moveMaskForward(terminal, wallsLevel1, maskar);
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

    public static void generateNewNumber(int value, Obstacles obstacles, Terminal terminal) throws IOException {

        Position numberPosition = null;
        boolean positionOk = false;
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

        //store the position of the next number
        for(Mask mask: maskar) {
            mask.setNumberPosition(numberPosition);
        }

        // printing numbers
        terminal.setForegroundColor(WHITE);
        terminal.setCursorPosition(numberPosition.x, numberPosition.y);
        terminal.putCharacter((char)(value + '0'));
        terminal.flush();
    }

    public static void updateScore(Terminal terminal) throws IOException {
        score += Main.value *100*100/speed;
        char[] scoreAsCharArray = ("" + score).toCharArray();
        //set cursor at top left corner with enough room left to print out the score
        terminal.setCursorPosition(terminal.getTerminalSize().getColumns()-6, 0);
        terminal.setForegroundColor(new TextColor.RGB(255, 255, 255));
        for(char c: scoreAsCharArray){
            terminal.putCharacter(c);
        }
    }







}
