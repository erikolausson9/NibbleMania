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
    public static int pointsToWin = 5;
    public static final int TOPOFPlAYINGFIELD=1;

    public static int speed;
    public static final TextColor WHITE = new TextColor.RGB(255, 255, 255);
    public static final TextColor PLAYERONE_COLOR = new TextColor.RGB(255, 0, 0); //make the player one mask red
    public static final TextColor PLAYERTWO_COLOR = new TextColor.RGB(0, 255, 0); //make the player two mask green
    public static final TextColor BOTMASK_COLOR = new TextColor.RGB(0, 0, 255); //make the botmask blue
    public static ArrayList<Mask> maskar;
    public static boolean twoPlayers;
    public static boolean playersWins;



    public static void main(String[] args) throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException {


        //initialize terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);


        //initialize screen and ask for 1 or 2 players
        twoPlayers = true;
        Screen screen = new Screen(terminal); //initialize new screen object
        if(!screen.startScreen(terminal)){ ////method call to start screen. Returns false if player wants to quit game.
            return;
        }

        //initialize screen to ask if player(s) want bot opponent. Return true if bot.
        boolean includeBot;
        includeBot = screen.botOrHumanOpponent(terminal);


        //Initialize screen to ask for user input about game difficulty. Return speed value.
        speed = screen.selectDifficulty(terminal);

        //adjust speed according to total number of worms
        if(twoPlayers && includeBot){
            speed /= 2.5;
        }
        else if(twoPlayers){
            speed /= 2;
        }
        else if(includeBot){
            speed /= 2;
        }

        //Initialize screen with instructions.
        screen.instructionsScreen(terminal);

        //display game title
        terminal.setForegroundColor(WHITE);
        String title = "NibbleMania";
        terminal.setCursorPosition(terminal.getTerminalSize().getColumns()/2-title.length()/2, 0);
        for(char c: title.toCharArray()){
            terminal.putCharacter(c);
        }

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
        BotMask botMask = null;

        if(includeBot){
            botMask = new BotMask(position3, speed, BOTMASK_COLOR);
            botMask.printMask(terminal);
            maskar.add(botMask);
        }


        //initialize walls for level 1
        Obstacles obstacles = new Obstacles(terminal);
        //generate random position without collisions for first number to catch
        generateNewNumber(1, obstacles, terminal);
        gameLoop(maskar, obstacles, twoPlayers, botMask, terminal);

        //Show end game screen
        screen.endScreen(terminal, playersWins);

        if(playersWins){
            String victorySound = "Victory.wav";
            SoundPlayer soundPlayer = new SoundPlayer();
            soundPlayer.playSound(victorySound);


            //move on to level 2
            terminal.clearScreen();
            obstacles.generateLevelTwoObstacles();
            //reinitialize mask(ar)
            maskar.clear();
            mask1 = new Mask(new Position(5, 5), speed, PLAYERONE_COLOR);
            maskar.add(mask1);
            mask1.printMask(terminal);
            if(twoPlayers){
                mask2 = new Mask(new Position(60, 15), speed, PLAYERTWO_COLOR);
                maskar.add(mask2);
                mask2.printMask(terminal);
            }
            if(includeBot){
                botMask = new BotMask(new Position(30, 18), speed, BOTMASK_COLOR);
                maskar.add(botMask);
                botMask.printMask(terminal);
            }

            obstacles.printObstacles(terminal);
            value = 1;
            playersWins = false;
            generateNewNumber(value, obstacles, terminal);
            gameLoop(maskar, obstacles, twoPlayers, botMask, terminal);

            screen.endScreen(terminal, playersWins);
            if(playersWins) {
                soundPlayer.playSound(victorySound);
            }

        }





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


    public static void gameLoop(ArrayList<Mask> maskar, Obstacles obstacles,
                                boolean twoPlayers, BotMask botMask, Terminal terminal) throws IOException {
        //variables used in gameloop
        KeyStroke keyStroke = null;
        KeyType type = null;
        char keyStrokeChar = ' ';
        boolean continuePlaying = true;

        //gameplay loop
        while (continuePlaying) {

            //check for keystroke from user(s)
            keyStroke = terminal.pollInput();
            if (keyStroke != null) {
                KeyType keyType = keyStroke.getKeyType();
                if (keyStroke.getCharacter() != null) {
                    keyStrokeChar = keyStroke.getCharacter();
                    //System.out.println("character: " + keyStrokeChar);
                    //Player two uses characters to change direction of the mask
                    if(twoPlayers){
                        maskar.get(1).changeDirectionPlayerTwo(keyStrokeChar);
                    }
                } else {
                    //Player one uses arrow keys to change direction of the mask
                    maskar.get(0).changeDirectionPlayerOne(keyType);
                }
            }



            if(twoPlayers){
                continuePlaying=maskar.get(0).moveMaskForward(terminal, obstacles, maskar)
                        && maskar.get(1).moveMaskForward(terminal, obstacles, maskar) ;
            }
            else
            {
                continuePlaying = maskar.get(0).moveMaskForward(terminal, obstacles, maskar);
            }

            //handle movement of the botMask
            if(botMask!=null && continuePlaying){
                botMask.setDirection();
                continuePlaying = botMask.moveMaskForward(terminal, obstacles, maskar);
            }

            //check if the player wants to quit the game
            if (keyStrokeChar == Character.valueOf('q')) {
                continuePlaying = false;
                System.out.println("quit");
                terminal.close();
            }


        }

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
