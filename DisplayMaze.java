import javax.*;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DisplayMaze extends JFrame
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        JButton b = new JButton("New Maze");
        Maze maze = new Maze();

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                maze.newMaze();
            }
        });

        frame.add(b, BorderLayout.NORTH);
        frame.add(maze);
        frame.setSize(600, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
