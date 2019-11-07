//-Mask: Skapa (start-storlek)
//-mask: koda rörelse
//-mask: funktion som växer x strl för varje siffra

import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import org.w3c.dom.ls.LSOutput;

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

    //constructor
    public Mask(Position startPosition, int speed){
        maskPositions= new ArrayList<>(initialMaskLength);
        maskPositions.add(startPosition);
        maskPositions.add(new Position(startPosition.x-1, startPosition.y));
        direction = MaskDirection.RIGHT;
        this.speed = speed;
        numberPosition = new Position(0,0);
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

    //instance methods
    public void printMask(Terminal terminal) throws IOException {

        for(Position pos: maskPositions){
            terminal.setCursorPosition(pos.x, pos.y);
            terminal.putCharacter(PLAYER);
        }
        terminal.flush();

    }
    public void changeDirectionOfMask(KeyType type){

        switch(type){
            case ArrowDown:
                if(direction==MaskDirection.LEFT || direction==MaskDirection.RIGHT) {
                    direction = MaskDirection.DOWN;
                }
                break;
            case ArrowUp:
                if(direction==MaskDirection.LEFT || direction==MaskDirection.RIGHT) {
                    direction = MaskDirection.UP;
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

    }

    public boolean moveMaskForward(Terminal terminal, Obstacles obstacles) throws InterruptedException, IOException {

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

        // check the collision with itself
        for(Position pos : maskPositions) {
            if (newX == pos.x  && newY == pos.y){
                System.out.println("GAME OVER!");
                return false;
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
            if(Main.value == 9) {
                System.out.println("YOU WON!!!");
                return false;
            }
            Main.updateScore(terminal);
            Main.value++;
            Main.generateNewNumber(Main.value, this, obstacles, terminal);
            currentMaskLength *= 2;

        }

        return true;

    }
}
