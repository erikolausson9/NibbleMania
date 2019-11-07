//-Mask: Skapa (start-storlek)
//-mask: koda rörelse
//-mask: funktion som växer x strl för varje siffra

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import org.w3c.dom.ls.LSOutput;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mask {



    //instance variables
    final char PLAYER = '\u2588';
    private final int initialMaskLength = 2;
    public int currentMaskLength = initialMaskLength;
    private List<Position> maskPositions;
    private MaskDirection direction;
    private int speed;
    private Position numberPosition;
    private TextColor color;

    //constructor
    public Mask(Position startPosition, int speed, TextColor color){
        maskPositions= new ArrayList<>(initialMaskLength);
        maskPositions.add(startPosition);
        maskPositions.add(new Position(startPosition.x-1, startPosition.y));
        direction = MaskDirection.RIGHT;
        this.speed = speed;
        numberPosition = new Position(0,0);
        this.color = color;
    }


    //getters and setters
    public List<Position> getMaskPositions() {
        return maskPositions;
    }

    public void setMaskPositions(List<Position> maskPositions) {
        this.maskPositions = maskPositions;
    }

    public Position getNumberPosition() {
        return numberPosition;
    }

    public void setNumberPosition(Position numberPosition) {
        this.numberPosition = numberPosition;
    }

    public MaskDirection getDirection() {
        return direction;
    }

    //instance methods
    public void printMask(Terminal terminal) throws IOException {

        for(Position pos: maskPositions){
            terminal.setCursorPosition(pos.x, pos.y);
            terminal.setForegroundColor(color);
            terminal.putCharacter(PLAYER);
        }
        terminal.flush();

    }
    public void changeDirectionPlayerOne(KeyType type){

        switch(type){
            case ArrowUp:
                if(direction==MaskDirection.LEFT || direction==MaskDirection.RIGHT) {
                    direction = MaskDirection.UP;
                }
                break;
            case ArrowDown:
                if(direction==MaskDirection.LEFT || direction==MaskDirection.RIGHT) {
                    direction = MaskDirection.DOWN;
                }
                break;
            case ArrowLeft:
                if(direction==MaskDirection.UP || direction==MaskDirection.DOWN) {
                    direction = MaskDirection.LEFT;
                }
                break;
            case ArrowRight:
                if(direction==MaskDirection.UP || direction==MaskDirection.DOWN) {
                    direction = MaskDirection.RIGHT;

                }


        }

    }    public void changeDirectionPlayerTwo(char keyStrokeChar){

        switch(keyStrokeChar){
            case 's':
                if(direction==MaskDirection.LEFT || direction==MaskDirection.RIGHT) {
                    direction = MaskDirection.DOWN;
                }
                break;
            case 'w':
                if(direction==MaskDirection.LEFT || direction==MaskDirection.RIGHT) {
                    direction = MaskDirection.UP;
                }
                break;
            case 'a':
                if(direction==MaskDirection.UP || direction==MaskDirection.DOWN) {
                    direction = MaskDirection.LEFT;
                }
                break;
            case 'd':
                if(direction==MaskDirection.UP || direction==MaskDirection.DOWN) {
                    direction = MaskDirection.RIGHT;

                }


        }

    }

    public boolean moveMaskForward(Terminal terminal, Obstacles obstacles, ArrayList<Mask> maskar)
            throws InterruptedException, IOException, LineUnavailableException, UnsupportedAudioFileException {

        Thread.sleep(speed);

        //get the position of the head of the mask
        int oldX = maskPositions.get(0).x;
        int oldY = maskPositions.get(0).y;

        int newX = oldX;
        int newY = oldY;

        //set new position depending on the direction of the mask
        switch (direction){
            case RIGHT:
                newX = oldX+1;
                break;
            case LEFT:
                newX = oldX-1;
                break;
            case DOWN:
                newY= oldY+1;
                break;
            case UP:
                newY=oldY-1;
        }
        // check wall collision
        for(Position pos : obstacles.getObstacles()) {
            if (newX == pos.x  && newY == pos.y){
                System.out.println("GAME OVER!");
                return false;
             }
        }

        // check the collision with itself and possible other mask
        for(Mask mask: maskar){
            for(Position pos : mask.getMaskPositions()) {
                if (newX == pos.x  && newY == pos.y){
                    System.out.println("GAME OVER!");
                    return false;
                }
            }
        }


        //add the new Mask position as the first element of the ArrayList
        maskPositions.add(0, new Position(newX, newY));
        if (maskPositions.size()>currentMaskLength){
            //platser vi vill sudda nedan:
            int x= maskPositions.get(maskPositions.size()-1).x; //plocka ut värdet för x från array-elementet
            int y= maskPositions.get(maskPositions.size()-1).y; //samma för y

            //put cursor where we want to clear
            terminal.setCursorPosition(x, y);
            terminal.putCharacter(' ');
            maskPositions.remove(maskPositions.size()-1);

        }
        printMask(terminal);

        //check if mask has eaten a number
        if(numberPosition.x == newX && numberPosition.y == newY) {
            //check to see if mask has eaten the final number
            if(Main.value == 9) {
                System.out.println("YOU WON!!!");
                //play victory
                String victorySound = "C:\\Users\\Erik Olausson\\IdeaProjects\\NibbleMania\\Victory.wav";
                SoundPlayer soundPlayer = new SoundPlayer();

                soundPlayer.playSound(victorySound);

                return false;
            }
            Main.updateScore(terminal);
            Main.value++;
            Main.generateNewNumber(Main.value, obstacles, terminal);
            currentMaskLength *= 2;

        }

        return true;

    }
}
