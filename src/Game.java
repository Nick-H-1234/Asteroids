/*
CLASS: Game
DESCRIPTION: A painted canvas in its own window, updated every tenth second.
USAGE: Extended by Asteroids.
NOTE: You don't need to understand the details here, no fiddling neccessary.
*/

import java.awt.*;
import java.awt.event.*;

abstract class Game extends Canvas {
    protected boolean on = true;
    protected int width, height;
    protected Image buffer;
    protected Frame frame;

    public Game(String name, int width, int height) {
        this.width = width;
        this.height = height;

        // Frame can be read as 'window' here.
        frame = new Frame(name);
        frame.add(this);
        frame.setSize(this.width, this.height);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        buffer = createImage(this.width, this.height);
    }

    // 'paint' will be called every tenth of a second that the game is on.
    abstract public void paint(Graphics brush);

    public void update(Graphics brush) {
        paint(buffer.getGraphics());
        brush.drawImage(buffer, 0, 0, this);
    }
}