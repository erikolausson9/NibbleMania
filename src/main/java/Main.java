import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;
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

        //initialize mask
        Mask mask = new Mask(new Position(10, 10), 100);
        mask.printMask(terminal);


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
                    mask.changeDirectionOfMask(type); //this should be a call to Mask class
                }
            }

            continuePlaying = mask.moveMaskForward(terminal);


            if (keyStrokeChar == Character.valueOf('q')) {
                continuePlaying = false;
                System.out.println("quit");
                terminal.close();
            }
        }

    }

//    public void generateNewNumber(int value, List<Mask> maskPositions, List<Walls> wallPositions ){
//        //Size 80 X 24
//
//        //Generate position for number
//        Position numberPosition = new Position(ThreadLocalRandom.current().nextInt(1, 79), ThreadLocalRandom.current().nextInt(1, 23));
//
//    }




}
