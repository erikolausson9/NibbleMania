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
    final char PLAYER = '\u2588'; //block used to represent parts of mask as well as obstacles
    private final int initialMaskLength = 2;
    public int currentMaskLength = initialMaskLength;
    private List<Position> maskPositions;
    protected MaskDirection direction; //used by botMask as well
    private int speed;
    private Position numberPosition;
    private TextColor color;
    private int score = 0;
    private int movement = 1;

    //constructor. Arguments are: starting position of mask, speed of mask and color of mask
    public Mask(Position startPosition, int speed, TextColor color){
        maskPositions= new ArrayList<>(initialMaskLength);
        maskPositions.add(startPosition);
        maskPositions.add(new Position(startPosition.x-1, startPosition.y)); //mask will start off along the horizontal axis
        direction = MaskDirection.RIGHT; //default initial direction of movement
        this.speed = speed;
        numberPosition = new Position(0,0); //this variable will be changed by the GenerateNewNumber method in the Main class
        this.color = color;
    }


    //getters and setters

    public int getScore(){
        return score;
    }
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
        //player one uses arrow keys, therefore the input to this method is a KeyType object
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
        //player two uses characters s, w, a and d to change direction. Therefore, the input to this method is a char
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
        //this method will return true if the mask could move forward without colliding with anything
        //and false if the mask collides with something, or has eaten the last number

        Thread.sleep(speed);

        //get the position of the head of the mask
        int oldX = maskPositions.get(0).x;
        int oldY = maskPositions.get(0).y;

        int newX = oldX;
        int newY = oldY;

        //set new position depending on the direction of the mask
        switch (direction){
            case RIGHT:
                newX = oldX+movement;
                break;
            case LEFT:
                newX = oldX-movement;
                break;
            case DOWN:
                newY= oldY+movement;
                break;
            case UP:
                newY=oldY-movement;
        }
        // check for obstacle collision
        for(Position pos : obstacles.getObstacles()) {
            if (newX == pos.x  && newY == pos.y){
                //System.out.println("GAME OVER!");
                if(this instanceof BotMask){
                    //if botMask has collided, stop its movements but continue game
                    movement=0;
                    return true;
                }
                return false;
             }
        }

        // check the collision with itself and possible other mask
        for(Mask mask: maskar){
            for(Position pos : mask.getMaskPositions()) {
                if (newX == pos.x  && newY == pos.y){
                    //System.out.println("GAME OVER!");
                    if(this instanceof BotMask){
                        //if the botMask has collided, stop its movements but continue game
                        movement=0;
                        return true;
                    }
                    return false;
                }
            }
        }


        //add the new Mask position as the first element of the ArrayList
        maskPositions.add(0, new Position(newX, newY));

        //check if we want to start clearing the tail of the mask
        if (maskPositions.size()>currentMaskLength){
            //clear this position:
            int x= maskPositions.get(maskPositions.size()-1).x; //extract the x-value of the last position of the mask
            int y= maskPositions.get(maskPositions.size()-1).y; //same for y-value

            //put cursor where we want to clear
            terminal.setCursorPosition(x, y);
            terminal.putCharacter(' ');
            //remove last position from the maskPositions ArrayList
            maskPositions.remove(maskPositions.size()-1);

        }
        printMask(terminal);

        //check if mask has eaten a number
        if(numberPosition.x == newX && numberPosition.y == newY) {

            //play eating sound
            String eatingSound = "eating.wav";
            SoundPlayer soundPlayer1 = new SoundPlayer();
            soundPlayer1.playSound(eatingSound);

            //check to see if mask has eaten the final number
            if(Main.value == Main.pointsToWin) {
                System.out.println("YOU WON!!!");

                //play victory
                String victorySound = "Victory.wav";
                SoundPlayer soundPlayer = new SoundPlayer();

                soundPlayer.playSound(victorySound);
                Main.value++;

                return false;
            }
            //mask has eaten a number but not the final number. Update score and generate a new number to catch
            score += Main.value *100;
            Main.updateScore(terminal);
            Main.value++;
            Main.generateNewNumber(Main.value, obstacles, terminal);
            currentMaskLength *= 2;

        }

        return true;

    }





}
