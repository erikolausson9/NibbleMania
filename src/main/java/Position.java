public class Position {
    //instance variables
    public int x;
    public int y;

    //constructor
    public Position(int x, int y){
        this.x= x;
        this.y=y;
    }
    // instance methods
    public boolean isTheSame(Position testPostion){
        return (this.x==testPostion.x && this.y==testPostion.y);
    }

}
