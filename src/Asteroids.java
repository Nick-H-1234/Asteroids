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
    Polygon invulnerableBox;

    int level = 1;
    int maxLives = 3;
    int score;
    int respawnTimer;


    public Asteroids() {
        super("Asteroids!", 800, 600);
        spawnBox = new Polygon(new Point[]{new Point(0, 0), new Point(width / 3.0, 0), new Point(width / 3.0, height / 3.0), new Point(0, height / 3.0)}, 1, new Point(width / 3.0, height / 3.0), 0, 0, new Velocity(0, 0));
        spawnBox.color = Color.green;
        invulnerableBox = new Polygon(new Point[]{new Point(0,0), new Point(30, 0), new Point (30, 150), new Point(0, 150)},1, new Point (10,100),0,0,new Velocity(0,0));
        invulnerableBox.color = Color.yellow;
        frame.addKeyListener(this);
        ship = new Ship(new Point(width / 2.0, height / 2.0), 1, new Velocity(0, 0));
        generateAsteroidsForLevel(level);
        resetLives(maxLives);
        score = 0;
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
        Point spawnBoxPosition = new Point(ship.position.getX() - (width / 6.0), ship.getPosition().getY() - (height / 6.0));
        spawnBox.setPosition(spawnBoxPosition);
        asteroidList.clear();
        int asteroidsNumber = (level * 2) + 2;
        for (int i = 0; i < asteroidsNumber; i++) {
            Asteroid asteroid = new Asteroid(generateShape(), 2, generateLocation(), new Velocity(generateRotation(), 0.9));
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
        return new Point(x, y);
    }

    public double generateRotation() {
        return Math.random() * 360;
    }

    public void paint(Graphics brush) {
        if (!gameOver) {
            brush.fillRect(0, 0, width, height);

            // draw our lives List and the score
            for (Ship life : livesList) {
                life.paint(brush);
            }
            brush.setFont(new Font("Font", Font.PLAIN, 20));
            brush.drawString(String.valueOf(score), width - 80, 40);

            // Handles bullet movements and collisions
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
                            newAsteroidsList.addAll(asteroid.split());
                        }
                        effectsList.addAll(makeExplosionEffects(asteroid.getPosition()));
                        asteroidIterator.remove();
                        bulletIterator.remove();
                        score += 100;
                        break;
                    }
                }
                asteroidList.addAll(newAsteroidsList);
            }

            //Asteroid movements
            for (Asteroid asteroid : asteroidList) {
                asteroid.move(width, height);
                asteroid.rotate();
                asteroid.paint(brush);
            }

            //Ship movements & respawning
            ship.shipTick();
            if (ship.isInvulnerable()) {
                ship.color = Color.yellow;
            } else {
                ship.color = Color.white;
            }
            if (ship.isAlive()) {
                ship.move(width, height);
                ship.rotate();
                ship.paint(brush);
            } else {
                if (respawnTimer == 0) {
                    ship.setAlive(true);
                    ship.reset(new Point(width / 2.0, height / 2.0));
                    ship.setInvulnerable(true);
                    ship.timer = 400;
                    spawnBox.setPosition(new Point(width / 3.0, height / 3.0));
                } else {
                    respawnTimer--;
                }
            }

            // ship - asteroid collisions
            for (Iterator<Asteroid> asteroidIterator = asteroidList.iterator(); asteroidIterator.hasNext(); ) {
                Asteroid asteroid = asteroidIterator.next();
                if (ship.isAlive() && asteroid.collidesWith(ship)) {
                    if (ship.isInvulnerable()) {
                        effectsList.addAll(makeExplosionEffects(asteroid.getPosition()));
                        score += (int) (150 * asteroid.scale);
                        asteroidIterator.remove();
                    } else if (livesList.size() > 1) {
                        effectsList.addAll(makeExplosionEffects(ship.getPosition()));
                        livesList.remove(livesList.size()-1);
                        ship.setAlive(false);
                        respawnTimer = 100;
                    } else {
                        brush.setColor(Color.red);
                        brush.drawString("GAME OVER", width / 2, height / 2);
                        brush.drawString("Press \"R\" to restart", width/2, height/2 + 30);
                        gameOver = true;
                    }
                }
            }

            // draw invulnerability meter
            if (ship.isInvulnerable()) {
                invulnerableBox.paint(brush);
                brush.setColor(Color.yellow);
                brush.fillRect(10,(int) (100+((400-ship.timer)/2.66666667)),31,(int) (151-(400-ship.timer)/2.66666667));
            }

            // level up
            if (asteroidList.isEmpty()) {
                level++;
                generateAsteroidsForLevel(level);
                score += 300;
            }

            // move & draw effects
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

    public List<Effect> makeExplosionEffects(Point position) {
        List<Effect> newEffects = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            double direction = i * (36 + Math.random() * 5);
            Effect effect = new Effect(1, position, new Velocity(direction, 1.2), 50);
            effect.color = Color.white;
            newEffects.add(effect);
        }
        return newEffects;
    }

    public void resetLives(int maxLives) {
        livesList.clear();
        for (int i = 0; i < maxLives; i++) {
            Ship spareShip = new Ship(new Point(10 + (i * 35), 10), 0.7, new Velocity(0, 0));
            spareShip.color = Color.white;
            livesList.add(spareShip);
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
        } else if (e.getKeyChar() == 'f' && ship.isAlive()) {
            bulletList.addAll(ship.shoot());
        } else if (e.getKeyChar() == 'r') {
            gameOver = false;
            level = 1;
            score = 0;
            resetLives(maxLives);
            bulletList.clear();
            ship.reset(new Point(width / 2.0, height / 2.0));
            ship.setRotation(-90);
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