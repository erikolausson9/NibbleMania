import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) throws IOException {

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
    }

    public void generateNewNumber(int value, List<Mask> maskPositions, List<Walls> wallPositions ){
        //Size 80 X 24

        //Generate position for number
        Position numberPosition = new Position(ThreadLocalRandom.current().nextInt(1, 79), ThreadLocalRandom.current().nextInt(1, 23));




    }

}
