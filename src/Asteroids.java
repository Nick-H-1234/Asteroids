/*
CLASS: Asteroids
DESCRIPTION: Extending Game, Asteroids is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.
*/

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Asteroids extends Game implements KeyListener {
    List<Asteroid> asteroidList = new ArrayList<>();
    List<Bullet> bulletList = new ArrayList<>();
    Ship ship;
    boolean gameOver = false;
    Polygon spawnBox;

    int level = 1;

    public Asteroids() {
        super("Asteroids!", 800, 600);
        spawnBox = new Polygon(new Point[]{new Point(0, 0), new Point(width / 3, 0), new Point(width / 3, height / 3), new Point(0, height / 3)}, new Point(width / 3, height / 3), 0, 1);
        spawnBox.color = Color.green;
        frame.addKeyListener(this);
        ship = new Ship(new Point(width / 2, height / 2), -90);
        generateAsteroidsForLevel(level);
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception exc) {
                    }
                    ;
                    Asteroids.this.repaint();
                }
            }
        }).start();
    }

    private void generateAsteroidsForLevel(int level) {
        Point spawnBoxPosition = ship.getPosition();
        //spawnBox.setPosition(spawnBoxPosition);
        asteroidList.clear();
        int asteroidsNumber = (level * 2) + 2;
        for (int i = 0; i < asteroidsNumber; i++) {

            Asteroid asteroid = new Asteroid(generateShape(), generateLocation(), generateRotation(), 2);
            if (asteroid.collidesWith(spawnBox)) {
                i--;
            } else {
                asteroidList.add(asteroid);
            }
        }
    }


    public Point[] generateShape() {
        return Math.random() < 0.5 ? Asteroid.ASTEROID_SHAPE_1 : Asteroid.ASTEROID_SHAPE_2;
    }

    public Point generateLocation() {
        double x = Math.random() * width;
        double y = Math.random() * height;
        return new Point((int) x, (int) y);
    }

    public int generateRotation() {
        return Math.random() < 0.5 ? 0 : 180;
    }

    public void paint(Graphics brush) {
        if (!gameOver) {
            brush.setColor(Color.black);
            brush.fillRect(0, 0, width, height);
            spawnBox.paint(brush);

            List<Asteroid> newAsteroidsList = new ArrayList<>();
            for (Iterator<Bullet> bulletIterator = bulletList.iterator(); bulletIterator.hasNext();) {
                Bullet bullet = bulletIterator.next();
                if (!bullet.move(width, height)) {
                    bulletIterator.remove();
                    continue;
                }
                bullet.paint(brush);
                newAsteroidsList.clear();
                for (Iterator<Asteroid> asteroidIterator = asteroidList.iterator(); asteroidIterator.hasNext();) {
                    Asteroid asteroid = asteroidIterator.next();
                    if (asteroid.collidesWith(bullet)) {
                        if (asteroid.scale > 1) {
                            Point point = asteroid.getPosition();
                            newAsteroidsList.add(new Asteroid(asteroid.getShape(),point,180,asteroid.getScale()/2));
                            newAsteroidsList.add(new Asteroid(asteroid.getShape(),point,0,asteroid.getScale()/2));
                        }
                        asteroidIterator.remove();
                        bulletIterator.remove();
                        break;
                    }
                }
                asteroidList.addAll(newAsteroidsList);
            }


            //brush.setColor(Color.white);
            for (Shape asteroid : asteroidList) {
                asteroid.move(width, height);
                asteroid.paint(brush);
            }

            ship.move(width, height);
            ship.rotate();
            ship.paint(brush);

            for (Shape asteroid : asteroidList) {
                if (ship.collidesWith(asteroid)) {
                    brush.setColor(Color.red);
                    brush.drawString("game over", width / 2, height / 2);
                    gameOver = true;
                }
            }
            if (asteroidList.isEmpty()) {
                level++;
                generateAsteroidsForLevel(level);
            }
        }
    }

    public static void main(String[] args) {
        new Asteroids();
    }

    public void shoot() {

        double rotation = ship.getRotation();
        Point position = ship.getTransformedPoints()[2];
        Bullet bullet = new Bullet(position, rotation);
        System.out.println("making bullet at position " + position + ", rotation " + rotation);
        bulletList.add(bullet);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 37) { //left arrow
            ship.setRotationSpeed(-2);
        } else if (e.getKeyCode() == 39) { //right arrow
            ship.setRotationSpeed(2);
        } else if (e.getKeyCode() == 38) { //up arrow
            ship.engineOn = true;
        } else if (e.getKeyChar() == 'f') {
            shoot();
        } else if (e.getKeyChar() == 'r') {
            gameOver = false;
            level = 1;
            generateAsteroidsForLevel(level);
            bulletList.clear();
            ship.setAcceleration(0);
            ship.setSpeed(0);
            ship.setXSpeed(0);
            ship.setYSpeed(0);
            ship.setPosition(new Point(width / 2, height / 2));
            ship.setYPos(ship.position.getY());
            ship.setXPos(ship.position.getX());
            ship.setRotation(-90);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 38) {
            ship.engineOn = false;
        }
        if (e.getKeyCode() == 37) { //left arrow
            ship.setRotationSpeed(0);
        } else if (e.getKeyCode() == 39) { //right arrow
            ship.setRotationSpeed(0);
        }
    }
}