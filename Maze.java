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

public class Maze extends JComponent
{
    ArrayList<MazePoint> points = new ArrayList<>();
    int wallThickness = 5;
    int widthHeight = 400;
    int startXY = 50;
    int wallSpace = 50;

    public Maze() { newMaze(); }

    private void FillPoints()
    {
        for(int i = startXY + wallSpace/2; i <= widthHeight + wallSpace/2; i += wallSpace)
            for(int j = startXY + wallSpace/2; j <= widthHeight + wallSpace/2; j += wallSpace)
                points.add(new MazePoint(i, j));
    }

    private void GenerateMaze()
    {
        HashSet<MazePoint> visited = new HashSet<>();
        Stack<MazePoint> path = new Stack<>();

        path.push(points.get(0));
        visited.add(points.get(0));

        while(visited.size() < points.size())
        {
            MazePoint top = path.peek();
            int rand = ThreadLocalRandom.current().nextInt(1, 5);
            
            if(rand == 1)
                TryToConnect(visited, path, top, wallSpace, 0);
            else if(rand == 2)
                TryToConnect(visited, path, top, 0, wallSpace);
            else if(rand == 3)
                TryToConnect(visited, path, top, -wallSpace, 0);
            else
                TryToConnect(visited, path, top, 0, -wallSpace);
            /*
            if(rand == 1)
                TryConnectRight(visited, path, top);
            else if(rand == 2)
                TryConnectDown(visited, path, top);
            else if(rand == 3)
                TryConnectLeft(visited, path, top);
            else
                TryConnectUp(visited, path, top);
            */
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

    private void TryConnectRight(HashSet<MazePoint> visited, Stack<MazePoint> path, MazePoint top)
    {
        if(!isPointVisited(visited, top.x + wallSpace, top.y))
            AddConnection(visited, path, top, top.x + wallSpace, top.y);
        else if(!isPointVisited(visited, top.x, top.y + wallSpace))
            AddConnection(visited, path, top, top.x, top.y + wallSpace);
        else if(!isPointVisited(visited, top.x - wallSpace, top.y))
            AddConnection(visited, path, top, top.x - wallSpace, top.y);
        else if(!isPointVisited(visited, top.x, top.y - wallSpace))
            AddConnection(visited, path, top, top.x, top.y - wallSpace);
        else
            path.pop();
    }

    private void TryConnectDown(HashSet<MazePoint> visited, Stack<MazePoint> path, MazePoint top)
    {
        if(!isPointVisited(visited, top.x, top.y + wallSpace))
            AddConnection(visited, path, top, top.x, top.y + wallSpace);
        else if(!isPointVisited(visited, top.x, top.y - wallSpace))
            AddConnection(visited, path, top, top.x, top.y - wallSpace);
        else if(!isPointVisited(visited, top.x - wallSpace, top.y))
            AddConnection(visited, path, top, top.x - wallSpace, top.y);
        else if(!isPointVisited(visited, top.x + wallSpace, top.y))
            AddConnection(visited, path, top, top.x + wallSpace, top.y);
        else
            path.pop();
    }

    private void TryConnectLeft(HashSet<MazePoint> visited, Stack<MazePoint> path, MazePoint top)
    {
        if(!isPointVisited(visited, top.x - wallSpace, top.y))
            AddConnection(visited, path, top, top.x - wallSpace, top.y);
        else if(!isPointVisited(visited, top.x, top.y - wallSpace))
            AddConnection(visited, path, top, top.x, top.y - wallSpace);
        else if(!isPointVisited(visited, top.x + wallSpace, top.y))
            AddConnection(visited, path, top, top.x + wallSpace, top.y);
        else if(!isPointVisited(visited, top.x, top.y + wallSpace))
            AddConnection(visited, path, top, top.x, top.y + wallSpace);
        else
            path.pop();
    }

    private void TryConnectUp(HashSet<MazePoint> visited, Stack<MazePoint> path, MazePoint top)
    {
        if(!isPointVisited(visited, top.x, top.y - wallSpace))
            AddConnection(visited, path, top, top.x, top.y - wallSpace);
        else if(!isPointVisited(visited, top.x - wallSpace, top.y))
            AddConnection(visited, path, top, top.x - wallSpace, top.y);
        else if(!isPointVisited(visited, top.x + wallSpace, top.y))
            AddConnection(visited, path, top, top.x + wallSpace, top.y);
        else if(!isPointVisited(visited, top.x, top.y + wallSpace))
            AddConnection(visited, path, top, top.x, top.y + wallSpace);
        else
            path.pop();
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
        if(x < startXY + wallSpace/2 || y < startXY + wallSpace/2)
            return true;
        if(x > widthHeight + wallSpace/2 || y > widthHeight + wallSpace/2)
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
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(wallThickness));
        g2.drawRect(startXY, startXY, widthHeight, widthHeight);
        DrawPoints(g2);
        DrawConnections(g2);
    }

    private void DrawPoints(Graphics2D g2)
    {
        for(int i = startXY + 25; i <= widthHeight + 25; i += wallSpace)
            for(int j = startXY + 25; j <= widthHeight + 25; j += wallSpace)
                g2.fillArc(i-5, j-5, 10, 10, 0, 360);
    }

    private void DrawConnections(Graphics2D g2)
    {
        for(int i = 0; i < points.size(); i++)
        {
            for(int j = 0; j < points.get(i).connections.size(); j++)
            {
                g2.drawLine(points.get(i).x, points.get(i).y,
                points.get(i).connections.get(j).x, points.get(i).connections.get(j).y);
            }
        }
    }

    private void DrawWalls(Graphics2D g2)
    {
        for(int i = 0; i < points.size(); i++)
        {

        }
    }

    private ArrayList<MazePoint> getNeighbors(MazePoint p)
    {
        ArrayList<MazePoint> nb = new ArrayList<>();

        return nb;
    }
}
