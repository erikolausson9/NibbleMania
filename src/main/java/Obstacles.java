import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Obstacles {
    private List<Position> obstacles;
    final char WALL = '\u2588';

    public List<Position> getObstacles() {
        return obstacles;
    }

    //constructor
    public Obstacles(Terminal terminal) throws IOException {
        obstacles = new ArrayList<Position>();

        generateOuterWalls();
        printObstacles(terminal);
    }

    public void generateOuterWalls() {

        for(int ii=0; ii<80; ii++){
            //create outer walls at top and bottom of screen
            obstacles.add(new Position(ii, Main.TOPOFPlAYINGFIELD));
            obstacles.add(new Position(ii, 23));
        }
        for(int ii=Main.TOPOFPlAYINGFIELD; ii<24; ii++){
            //create outer walls at left and right edges of screen
            obstacles.add(new Position(0, ii));
            obstacles.add(new Position(79, ii));
        }

    }

    public void generateLevelTwoObstacles(){
        for(int ii=0; ii<10; ii++){
            obstacles.add(new Position(20, ii+5));
            obstacles.add(new Position(40, ii+5));
            obstacles.add(new Position(60, ii+5));

        }
    }

    public void printObstacles(Terminal terminal) throws IOException {
        terminal.setForegroundColor(Main.WHITE);
        for (Position p: obstacles){
            terminal.setCursorPosition(p.x, p.y);
            terminal.putCharacter(WALL);
        }
        terminal.flush();
    }


}
