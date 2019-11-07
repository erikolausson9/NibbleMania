import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

    public class Screen {

        //instance variables
        private int terminalColumns;
        private int terminalRows;

        //constructor
        public Screen(Terminal terminal) throws IOException, InterruptedException {

            this.terminalColumns = terminal.getTerminalSize().getColumns();
            this.terminalRows = terminal.getTerminalSize().getRows();
        }

        public void startScreen(Terminal terminal) throws IOException, InterruptedException {
            String row1 = "Welcome to Nibble Mania!";
            int row1Length = row1.toCharArray().length;

            terminal.setCursorPosition((terminalColumns/2 - row1Length/2), terminalRows-15);
            for (char c : row1.toCharArray()) {
                terminal.putCharacter(c);
            }

            String row2 = "Press enter to start new game";
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

                if(type == KeyType.Enter){
                    terminal.clearScreen();
                    break;
                }
                else if(c == 'q'){
                    System.out.println("quit");
                    terminal.close();
                }
            }
        }

    }
