import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DisplayMaze extends JFrame
{
    private static Maze maze;
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JButton b = new JButton("New Maze");
        JButton b2 = new JButton("Solve Maze");
        maze = new Maze();

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                /* we don't want this function to interrupt our main thread */
                Thread t = new Thread() {
                    public void run()
                    {
                        maze.newMaze();
                    }
                };
                t.start();
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                /* we don't want this function to interrupt our main thread */
                Thread t = new Thread() {
                    public void run()
                    {
                        maze.SolveMaze();
                    }
                };
                t.start();
            }
        });

        panel.add(b);
        panel.add(b2);
        frame.add(maze);
        frame.add(panel, BorderLayout.NORTH);
        frame.setSize(600, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
