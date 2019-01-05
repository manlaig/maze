import java.util.ArrayList;

public class MazePoint
{
    public int x, y;
    public ArrayList<MazePoint> connections;

    public MazePoint(int xx, int yy)
    {
        x = xx;
        y = yy;
        connections = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj)
    {
        MazePoint p = (MazePoint) obj;
        if(x == p.x && y == p.y)
            return true;
        return false;
    }

    @Override
    public String toString()
    {
        return x + " " + y;
    }

    @Override
    public int hashCode()
    {
        /* need to always return a constant value,
           so the set of visited points doesn't contain duplicates */
        return 0;
    }
}