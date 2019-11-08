import com.googlecode.lanterna.TextColor;

public class BotMask extends Mask {

    //constructor
    public BotMask(Position startPosition, int speed, TextColor color){
        super(startPosition, speed, color);
    }

    //instance methods
    public void setDirection(){
        //find the position of the number we want to catch
        int goalX = getNumberPosition().x;
        int goalY = getNumberPosition().y;

        int currentX = getMaskPositions().get(0).x;
        int currentY = getMaskPositions().get(0).y;


        switch(direction){
            case UP:
            case DOWN:
                //if botMask is moving up or down: change direction based on where we want to go in the horizontal direction
                if(currentX>goalX){
                    direction = MaskDirection.LEFT;
                }
                else if(currentX<goalX){
                    direction = MaskDirection.RIGHT;
                }
                break;
            case LEFT:
            case RIGHT:
                //if botMask is moving up or down: change direction based on where we want to go in the vertical direction
                if(currentY>goalY){
                    direction = MaskDirection.UP;
                }
                else if(currentY<goalY){
                    direction = MaskDirection.DOWN;
                }
                break;
        }

    }

}
