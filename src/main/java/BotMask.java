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

        System.out.printf("goalx %d, goaly %d, currentx %d, currenty %d \n", goalX, goalY, currentX, currentY);

        switch(direction){
            case UP:
            case DOWN:
                if(currentX>goalX){
                    direction = MaskDirection.LEFT;
                }
                else if(currentX<goalX){
                    direction = MaskDirection.RIGHT;
                }
                break;
            case LEFT:
            case RIGHT:
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
