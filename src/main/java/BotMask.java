import com.googlecode.lanterna.TextColor;

public class BotMask extends Mask {

    //constructor
    public BotMask(Position startPosition, int speed, TextColor color){
        super(startPosition, speed, color);
    }

    //instance methods
    public void setDirection(){
        int goalX = getNumberPosition().x;
        int goalY = getNumberPosition().y;

        int currentX = getMaskPositions().get(0).x;
        int currentY = getMaskPositions().get(0).y;

        switch(getDirection()){
            case RIGHT:
                if()
        }

    }

}
