import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        

        //initialize terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();

        terminal.setCursorVisible(false);
        KeyStroke keyStroke = null;

        char keyStrokeChar = ' ';
        boolean continuePlaying = true;

        //gameplay loop
        while(continuePlaying){

            //check for keystroke from user
            keyStroke = terminal.pollInput();
            if(keyStroke!=null) {
                KeyType type = keyStroke.getKeyType();
                if(keyStroke.getCharacter()!=null) {
                    keyStrokeChar = keyStroke.getCharacter();
                    System.out.println("character: " + keyStrokeChar);
                }
                else {
                    System.out.println("type: " + type);
                    //changeDirectionOfMask(type); //this should be a call to Mask class
                }
            }





            if(keyStrokeChar==Character.valueOf('q')){
                continuePlaying=false;
                System.out.println("quit");
                terminal.close();
            }
        }



    }



}
