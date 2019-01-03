import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

class MazePoint
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
    public int hashCode()
    {
        /* need to always return a constant value,
           so the set of visited points doesn't contain duplicates */
        return 0;
    }
}

public class Maze extends JPanel
{
    ArrayList<MazePoint> points = new ArrayList<>();
    int wallThickness = 30;
    int widthHeight = 400;
    int startXY = 50;
    int rowsCols = 10; // dimensions of the maze
    int wallSpace = widthHeight / rowsCols;

    public Maze() { newMaze(); }

    private void FillPoints()
    {
        for(int i = 1; i < rowsCols; i++)
            for(int j = 1; j < rowsCols; j++)
                points.add(new MazePoint(startXY + wallSpace * i, startXY + wallSpace * j));
    }

    private void GenerateMaze()
    {
        HashSet<MazePoint> visited = new HashSet<>();
        Stack<MazePoint> path = new Stack<>();

        path.push(points.get(0));
        visited.add(points.get(0));

        while(visited.size() < points.size() && path.size() > 0)
        {
            MazePoint top = path.peek();
            int rand = ThreadLocalRandom.current().nextInt(1, 5);
            
            if(rand == 1)
                TryToConnect(visited, path, top, wallSpace, 0); // right
            else if(rand == 3)
                TryToConnect(visited, path, top, 0, wallSpace); // down
            else if(rand == 2)
                TryToConnect(visited, path, top, -wallSpace, 0); // left
            else
                TryToConnect(visited, path, top, 0, -wallSpace); // up
        }
    }

    private void TryToConnect(HashSet<MazePoint> visited, Stack<MazePoint> path, MazePoint p, int prefX, int prefY)
    {
        if(prefX != 0)
        {
            if(!isPointVisited(visited, p.x + prefX, p.y))
                AddConnection(visited, path, p, p.x + prefX, p.y);
            else if(!isPointVisited(visited, p.x, p.y + prefX))
                AddConnection(visited, path, p, p.x, p.y + prefX);
            else if(!isPointVisited(visited, p.x - prefX, p.y))
                AddConnection(visited, path, p, p.x - prefX, p.y);
            else if(!isPointVisited(visited, p.x, p.y - prefX))
                AddConnection(visited, path, p, p.x, p.y - prefX);
            else
                path.pop();
        }
        else
        {
            if(!isPointVisited(visited, p.x, p.y + prefY))
                AddConnection(visited, path, p, p.x, p.y + prefY);
            else if(!isPointVisited(visited, p.x + prefX, p.y))
                AddConnection(visited, path, p, p.x + prefX, p.y);
            else if(!isPointVisited(visited, p.x, p.y - prefY))
                AddConnection(visited, path, p, p.x, p.y - prefY);
            else if(!isPointVisited(visited, p.x - prefY, p.y))
                AddConnection(visited, path, p, p.x - prefY, p.y);
            else
                path.pop();
        }
    }

    private void AddConnection(HashSet<MazePoint> visited, Stack<MazePoint> path, MazePoint p, int x, int y)
    {
        int indexOfOrigin = points.indexOf(p);
        int indexOfDest = points.indexOf(new MazePoint(x, y));
        if(indexOfOrigin == -1 || indexOfDest == -1)
        {
            System.out.println("Point not found");
            return;
        }
        MazePoint pRef = points.get(indexOfDest);
        points.get(indexOfOrigin).connections.add(pRef);
        path.push(pRef);
        visited.add(pRef);
    }

    private boolean isPointVisited(HashSet<MazePoint> visited, int x, int y)
    {
        if(x < startXY + wallSpace || y < startXY + wallSpace)
            return true;
        if(x > startXY + wallSpace * (rowsCols-1) || y > startXY + wallSpace * (rowsCols-1))
            return true;
        if(visited.contains(new MazePoint(x, y)))
            return true;
        return false;
    }

    public void newMaze()
    {
        points.clear();
        FillPoints();
        GenerateMaze();
        repaint();
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        // background
        g.setColor(Color.white);
        g.fillRect(0, 0, DisplayMaze.width, DisplayMaze.height);
        g.setColor(Color.black);
        g.fillRect(startXY, startXY, widthHeight, widthHeight);
        g.setColor(Color.white);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(wallThickness));
        g2.drawRect(startXY, startXY, widthHeight, widthHeight);
        DrawConnections(g2);
    }

    private void DrawConnections(Graphics2D g2)
    {
        // drawing the start of the maze
        if(points.get(0).connections.get(0).x - points.get(0).x > 0)
            g2.drawLine(points.get(0).x, points.get(0).y, points.get(0).x-10, points.get(0).y);
        else
            g2.drawLine(points.get(0).x, points.get(0).y, points.get(0).x, points.get(0).y-10);

        // drawing the end of the maze
        int last = points.size() - 1;
        g2.drawLine(points.get(last).x, points.get(last).y, points.get(last).x+10, points.get(last).y);

        for(int i = 0; i < points.size(); i++)
        {
            for(int j = 0; j < points.get(i).connections.size(); j++)
            {
                g2.drawLine(points.get(i).x, points.get(i).y,
                points.get(i).connections.get(j).x, points.get(i).connections.get(j).y);
            }
        }
    }
}
