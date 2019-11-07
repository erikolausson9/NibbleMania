import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

//import javax.sound.sampled.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static int value = 1;
    public static final int TOPOFPlAYINGFIELD=1;
    public static int score = 0;
    public static int speed = 150;

    public static void main(String[] args) throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException, LineUnavailableException, UnsupportedAudioFileException {

        //initialize sound
        String victorySound = "C:\\Users\\Erik Olausson\\IdeaProjects\\NibbleMania\\Victory.wav";
        SoundPlayer soundPlayer = new SoundPlayer();

        soundPlayer.playSound(victorySound);

        //initialize terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();


        terminal.setCursorVisible(false);
        //display score
        terminal.setCursorPosition(terminal.getTerminalSize().getColumns()-14, 0);
        String points = "Points: ";
        points += Integer.toString(score);
        for(char c : points.toCharArray()){
            terminal.putCharacter(c);
        }




        KeyStroke keyStroke = null;

        char keyStrokeChar = ' ';
        boolean continuePlaying = true;



        //initialize wallsLevel1
        Obstacles wallsLevel1 = new Obstacles(terminal);

        //initialize mask
        Position position1 = new Position(10, 10);
        Mask mask = new Mask(position1, 100);
        mask.printMask(terminal);
        generateNewNumber(1, mask, wallsLevel1, terminal);

        //gameplay loop
        while (continuePlaying) {

            //check for keystroke from user
            keyStroke = terminal.pollInput();
            if (keyStroke != null) {
                KeyType type = keyStroke.getKeyType();
                if (keyStroke.getCharacter() != null) {
                    keyStrokeChar = keyStroke.getCharacter();
                    System.out.println("character: " + keyStrokeChar);
                } else {
                    System.out.println("type: " + type);
                    //call the Mask class and see if the direction should change
                    mask.changeDirectionOfMask(type);
                }
            }

            continuePlaying = mask.moveMaskForward(terminal, wallsLevel1);

            //check if the player wants to quit the game
            if (keyStrokeChar == Character.valueOf('q')) {
                continuePlaying = false;
                System.out.println("quit");
                terminal.close();
            }
        }
        terminal.close();
    }

    public static void generateNewNumber(int value, Mask mask, Obstacles obstacles, Terminal terminal) throws IOException {

        Position numberPosition = null;
        boolean positionOk = false;
        while(!positionOk) {
            positionOk = true;
            numberPosition = new Position(ThreadLocalRandom.current().nextInt(0, terminal.getTerminalSize().getColumns()-1), ThreadLocalRandom.current().nextInt(TOPOFPlAYINGFIELD, terminal.getTerminalSize().getRows()-1));


            System.out.println("Looping");
            //make sure new number position doesn't collide with any walls or other obstacles
            for(Position pos : obstacles.getObstacles()) {
                if (numberPosition.x == pos.x  && numberPosition.y == pos.y){
                    positionOk = false;
                    break;
                }
            }

            //make sure the new number position doesn't collide with mask
            for(Position pos : mask.getMaskPositions()) {
                if (numberPosition.x == pos.x  && numberPosition.y == pos.y){
                    positionOk = false;
                    break;
                }
            }
        }

        mask.setNumberPosition(numberPosition);

        // printing numbers
        terminal.setCursorPosition(numberPosition.x, numberPosition.y);
        terminal.putCharacter((char)(value + '0'));
        terminal.flush();
    }

    public static void updateScore(Terminal terminal) throws IOException {
        score += Main.value *100*100/speed;
        char[] scoreAsCharArray = ("" + score).toCharArray();
        //set cursor at top left corner with enough room left to print out the score
        terminal.setCursorPosition(terminal.getTerminalSize().getColumns()-6, 0);
        for(char c: scoreAsCharArray){
            terminal.putCharacter(c);
        }
    }







}
