import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class Maze extends JPanel
{
    ArrayList<MazePoint> points = new ArrayList<>();
    Stack<MazePoint> solutionPath = new Stack<>();
    int wallThickness = 30;
    int widthHeight = 400;
    int startXY = 75;
    int rowsCols = 10; // dimensions of the maze
    int wallSpace = widthHeight / rowsCols;

    public Maze()
    {
        newMaze();
    }

    private void FillPoints()
    {
        for(int i = 1; i < rowsCols; i++)
            for(int j = 1; j < rowsCols; j++)
                points.add(new MazePoint(startXY + wallSpace * i, startXY + wallSpace * j));
    }

    /* fills the solutionPath stack with the correct path to the end */
    synchronized public void SolveMaze()
    {
        MazePoint start = points.get(0);
        MazePoint endMaze = points.get(points.size() - 1);
        HashSet<MazePoint> visited = new HashSet<>();

        solutionPath.clear();
        solutionPath.push(start);
        visited.add(start);
        MazePoint curr = start;

        while(!solutionPath.peek().equals(endMaze))
        {
            ArrayList<MazePoint> current = curr.connections;
            if(current.size() == 0 || allVisited(current, visited))
            {
                // if we hit a dead end, end backtrack
                solutionPath.pop();
                curr = solutionPath.peek();
            }
            for(int i = 0; i < current.size(); i++)
                if(!visited.contains(current.get(i)))
                {
                    solutionPath.push(current.get(i));
                    visited.add(current.get(i));
                    curr = current.get(i);
                    
                    break;
                }
            
                /* redrawing with delay to show the process of the algorithm */
                reDraw();
                sleepMilliseconds(75);
        }
    }

    boolean allVisited(ArrayList<MazePoint> arr, HashSet<MazePoint> visited)
    {
        for(int i = 0; i < arr.size(); i++)
            if(!visited.contains(arr.get(i)))
                return false;
        return true;
    }

    private void GenerateMaze()
    {
        HashSet<MazePoint> visited = new HashSet<>();
        Stack<MazePoint> path = new Stack<>();

        path.push(points.get(0));
        visited.add(points.get(0));

        while(path.size() > 0 && visited.size() < points.size())
        {
            MazePoint top = path.peek();

            /* randomly choose which neighboring point to visit */
            int rand = ThreadLocalRandom.current().nextInt(1, 5);
            /* trying to connect to the neighboring points */
            /* will fail to connect if the point is at the edge or already visited */
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

        /* showing the process of generating the maze */
        reDraw();
		sleepMilliseconds(30);
    }

    private void AddConnection(HashSet<MazePoint> visited, Stack<MazePoint> path, MazePoint p, int x, int y)
    {
        int indexOfOrigin = points.indexOf(p);
        int indexOfDest = points.indexOf(new MazePoint(x, y));
        if(indexOfOrigin == -1 || indexOfDest == -1)
            return;

        MazePoint pRef = points.get(indexOfDest);
        points.get(indexOfOrigin).connections.add(pRef);
        path.push(pRef);
        visited.add(pRef);
    }
    
    private boolean isPointVisited(HashSet<MazePoint> visited, int x, int y)
    {
        return (Math.min(x, y) < startXY + wallSpace ||
                Math.max(x, y) > startXY + wallSpace * (rowsCols-1))
                ? true : visited.contains(new MazePoint(x, y));
    }

    /*
    we can only generate 1 maze at a time.
    when multiple threads all try to execute this function, it gets messy
    */
    synchronized public void newMaze()
    {
        solutionPath.clear();
        points.clear();
        FillPoints();
        GenerateMaze();
        repaint();
    }

    public void paint(Graphics g)
    {
        if(g == null)
            return;

        super.paint(g);

        // background
        g.setColor(Color.white);
        g.fillRect(0, 0, getBounds().width, getBounds().height);
        g.setColor(Color.black);
        g.fillRect(startXY, startXY, widthHeight, widthHeight);
        g.setColor(Color.white);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(wallThickness));
        g2.drawRect(startXY, startXY, widthHeight, widthHeight);

        // drawing the start of the maze
        MazePoint start = points.get(0);
        if(start.connections.get(0).x - start.x > 0)
            g2.drawLine(start.x, start.y, start.x-100, start.y);
        else
            g2.drawLine(start.x, start.y, start.x, start.y-100);

        // drawing the end of the maze
        MazePoint last = points.get(points.size() - 1);
        g2.drawLine(last.x, last.y, last.x+100, last.y);

        DrawConnections(g2);

        if(!solutionPath.empty())
        {
            Object[] pArr = solutionPath.toArray();
            g2.setColor(Color.red);
            g2.setStroke(new BasicStroke(10));
            for(int i = 0; i < pArr.length - 1; i++)
                g2.drawLine(((MazePoint)pArr[i]).x, ((MazePoint)pArr[i]).y,
                            ((MazePoint)pArr[i+1]).x, ((MazePoint)pArr[i+1]).y);
            g2.setColor(Color.black);
        }
    }

    private void DrawConnections(Graphics2D g2)
    {
        for(int i = 0; i < points.size(); i++)
            for(int j = 0; j < points.get(i).connections.size(); j++)
            {
                MazePoint curr = points.get(i);
                g2.drawLine(curr.x, curr.y, curr.connections.get(j).x, curr.connections.get(j).y);
            }
    }

    private void reDraw()
    {
        /*
        repaint() is not called fast enough to show the process of the algorithm.
        there occurs a laggy effect when calling paint in the main thread.
        */
        Thread t = new Thread() 
        {
            public void run()
            {
                paint(getGraphics());
            }
        };
        t.start();
    }

    private void sleepMilliseconds(int delay)
	{
		long timeStart = System.currentTimeMillis();
		while(System.currentTimeMillis() - timeStart < delay);
	}
}
