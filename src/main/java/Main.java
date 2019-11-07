import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        //initialize terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();

        terminal.setCursorVisible(false);
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
    }

    public static void generateNewNumber(int value, Mask mask, Obstacles obstacles, Terminal terminal) throws IOException {

        Position numberPosition = null;
        boolean positionOk = false;
        while(!positionOk) {
            positionOk = true;
            numberPosition = new Position(ThreadLocalRandom.current().nextInt(0, 80), ThreadLocalRandom.current().nextInt(0, 24));

            // check wall collision
            for(Position pos : obstacles.getObstacles()) {
                if (numberPosition.x == pos.x  && numberPosition.y == pos.y){
                    positionOk = false;
                    break;
                }
            }

            // check the collision with itself
            for(Position pos : mask.getMaskPositions()) {
                if (numberPosition.x == pos.x  && numberPosition.y == pos.y){
                    positionOk = false;
                    break;
                }
            }
        }

        // printing numbers
        terminal.setCursorPosition(numberPosition.x, numberPosition.y);
        terminal.putCharacter((char)(value + '0'));
        terminal.flush();

    }

//    public void generateNewNumber(int value, List<Mask> maskPositions, List<Walls> wallPositions ){
//        //Size 80 X 24
//
//        //Generate position for number
//        Position numberPosition = new Position(ThreadLocalRandom.current().nextInt(1, 79), ThreadLocalRandom.current().nextInt(1, 23));
//
//    }




}
