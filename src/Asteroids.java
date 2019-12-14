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
    List<Effect> effectsList = new ArrayList<>();
    List<Ship> livesList = new ArrayList<>();
    Ship ship;
    boolean gameOver = false;
    Polygon spawnBox;

    int level = 1;
    int maxLives = 3;
    int lives;
    int score;


    public Asteroids() {
        super("Asteroids!", 800, 600);
        spawnBox = new Polygon(new Point[]{new Point(0, 0), new Point(width / 3, 0), new Point(width / 3, height / 3), new Point(0, height / 3)}, new Point(width / 3, height / 3), 0, 1);
        spawnBox.color = Color.green;
        frame.addKeyListener(this);
        ship = new Ship(new Point(width / 2, height / 2), -90);
        generateAsteroidsForLevel(level);
        lives = maxLives;
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
        Point spawnBoxPosition = new Point(ship.position.getX() - (width / 6), ship.getPosition().getY() - (height / 6));
        spawnBox.setPosition(spawnBoxPosition);
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
        return Math.random() < 0.5 ? Asteroid.ASTEROID_SHAPE_3 : Asteroid.ASTEROID_SHAPE_4;
    }

    public Point generateLocation() {
        double x = Math.random() * width;
        double y = Math.random() * height;
        return new Point((int) x, (int) y);
    }

    public double generateRotation() {
        return Math.random() < 0.5 ? 0 : 180;
        //return Math.random()*360;
    }

    public void paint(Graphics brush) {
        if (!gameOver) {
            brush.setColor(Color.black);
            brush.fillRect(0, 0, width, height);
            spawnBox.paint(brush);

            livesList.clear();
            for (int i = 0; i < lives; i++) {
                Ship spareShip = new Ship(new Point(10 + (i * 50), 10), -90);
                spareShip.color = Color.white;
                livesList.add(spareShip);
            }
            for (Ship life : livesList) {
                life.paint(brush);
            }

            List<Asteroid> newAsteroidsList = new ArrayList<>();
            for (Iterator<Bullet> bulletIterator = bulletList.iterator(); bulletIterator.hasNext(); ) {
                Bullet bullet = bulletIterator.next();
                if (!bullet.move(width, height)) {
                    bulletIterator.remove();
                    continue;
                }
                bullet.paint(brush);
                newAsteroidsList.clear();
                for (Iterator<Asteroid> asteroidIterator = asteroidList.iterator(); asteroidIterator.hasNext(); ) {
                    Asteroid asteroid = asteroidIterator.next();
                    if (asteroid.collidesWith(bullet)) {
                        if (asteroid.scale > 1) {
                            Point point = asteroid.getPosition();
                            double rotation = asteroid.getRotation();
                            newAsteroidsList.add(new Asteroid(asteroid.getShape(), point,rotation+10, asteroid.getScale() / 2)); //todo fix rotation
                            newAsteroidsList.add(new Asteroid(asteroid.getShape(), point, rotation , asteroid.getScale() / 2));
                        }
                        explode(asteroid.getPosition());
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
                if (ship.collidesWith(asteroid) && lives > 1) {
                    explode(ship.getPosition());
                    lives--;
                    spawnBox.setPosition(new Point(width / 3, height / 3));
                    ship.reset(new Point(width / 2, height / 2));
                } else if (ship.collidesWith(asteroid)) {
                    brush.setColor(Color.red);
                    brush.drawString("game over", width / 2, height / 2);
                    gameOver = true;
                }
            }

            if (asteroidList.isEmpty()) {
                level++;
                generateAsteroidsForLevel(level);
            }

            for (Iterator<Effect> effectIterator = effectsList.iterator(); effectIterator.hasNext(); ) {
                Effect effect = effectIterator.next();
                if (!effect.move(width, height)) {
                    effectIterator.remove();
                    continue;
                }
                effect.paint(brush);
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
        bulletList.add(bullet);
    }

    public void explode(Point position) {
        for (int i = 0; i < 10; i++) {
            Effect effect = new Effect(position, 1, i * (36 + Math.random()*5), 50);
            effect.color = Color.white;
            effect.setSpeed(1.2);
            effectsList.add(effect);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 37) { //left arrow
            ship.setRotationSpeed(-2.5);
        } else if (e.getKeyCode() == 39) { //right arrow
            ship.setRotationSpeed(2.5);
        } else if (e.getKeyCode() == 38) { //up arrow
            ship.engineOn = true;
        } else if (e.getKeyChar() == 'f') {
            shoot();
        } else if (e.getKeyChar() == 'r') {
            gameOver = false;
            level = 1;
            lives = maxLives;
            bulletList.clear();
            ship.reset(new Point(width / 2, height / 2));
            generateAsteroidsForLevel(level);
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