import java.util.ArrayList;
import java.util.List;

public class Obstacles {
    private List<Position> obstacles;

    //constructor
    public Obstacles(){
        obstacles = new ArrayList<Position>();

        generateOuterWalls();
    }

    private void generateOuterWalls() {

        for(int ii=0; ii<80; ii++){
            //create outer walls at top and bottom of screen
            obstacles.add(new Position(ii, 0));
            obstacles.add(new Position(ii, 24));
        }
        for(int ii=0; ii<80; ii++){
            //create outer walls at top and bottom of screen
            obstacles.add(new Position(ii, 0));
            obstacles.add(new Position(ii, 24));
        }


    }


}
