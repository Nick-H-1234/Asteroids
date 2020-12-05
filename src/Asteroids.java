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

import static java.awt.Color.white;

class Asteroids extends Game implements KeyListener {
    List<Asteroid> asteroidList = new ArrayList<>();
    List<Bullet> bulletList = new ArrayList<>();
    List<Effect> effectsList = new ArrayList<>();
    List<Polygon> polygonEffectsList = new ArrayList<>();
    List<Ship> livesList = new ArrayList<>();
    Ship ship;
    boolean gameOver = false;
    Polygon spawnBox;
    Polygon invulnerableBox;

    int level = 1;
    int maxLives = 3;
    int score;

    public Asteroids() {
        super("Asteroids!", 800, 600);
        spawnBox = new Polygon(new Point[]{new Point(0, 0), new Point(width / 3.0, 0), new Point(width / 3.0, height / 3.0), new Point(0, height / 3.0)}, 1, new Point(width / 3.0, height / 3.0), 0, 0, new Velocity(0, 0));
        spawnBox.color = Color.green;
        invulnerableBox = new Polygon(new Point[]{new Point(0, 0), new Point(30, 0), new Point(30, 150), new Point(0, 150)}, 1, new Point(10, 100), 0, 0, new Velocity(0, 0));
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
                    Asteroids.this.repaint();
                    frame.addFocusListener(new FocusListener(){
                        public void focusGained(FocusEvent e){
                        }
                        public void focusLost(FocusEvent e){
                            e.getComponent().requestFocus();
                        }
                    });
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        new Asteroids();
    }

    public void paint(Graphics brush) {
        if (!gameOver) {
            brush.fillRect(0, 0, width, height);

            // Draw our lives List and the score
            for (Ship life : livesList) {
                life.paint(brush);
            }
            brush.setFont(new Font("Font", Font.PLAIN, 20));
            brush.drawString(String.valueOf(score), width - 80, 40);
            if(!ship.isAlive() && ship.getRespawnTimer() == 0) {
                brush.drawString("Press any key to respawn", width/2 - 80, 40);
            }

            // Handles asteroid movement and collisions
            List<Asteroid> newAsteroidsList = new ArrayList<>();
            for (Iterator<Asteroid> asteroidIterator = asteroidList.iterator(); asteroidIterator.hasNext(); ) {
                Asteroid asteroid = asteroidIterator.next();
                asteroid.move(width, height);
                asteroid.rotate();
                asteroid.paint(brush);
                if (ship.isAlive() && asteroid.collidesWith(ship)) {
                    if (ship.isInvulnerable()) {
                        effectsList.addAll(makeExplosionEffects(asteroid.getPosition()));
                        if (asteroid.getScale() == 2) {
                            score += 700;
                        } else if (asteroid.getScale() == 1.5) {
                            score += 300;
                        } else {
                            score += 100;
                        }
                        asteroidIterator.remove();
                    } else if (livesList.size() > 1) {
                        effectsList.addAll(makeExplosionEffects(ship.getPosition()));
                        livesList.remove(livesList.size() - 1);
                        polygonEffectsList.addAll(ship.die());
                        spawnBox.setPosition(new Point(width / 3.0, height / 3.0));
                    } else {
                        brush.setColor(Color.red);
                        brush.drawString("GAME OVER", width / 2, height / 2);
                        brush.drawString("Press \"R\" to restart", width / 2, height / 2 + 30);
                        gameOver = true;
                    }
                }

                // Handles bullet-asteroid collisions
                for (Iterator<Bullet> bulletIterator = bulletList.iterator(); bulletIterator.hasNext(); ) {
                    Bullet bullet = bulletIterator.next();
                    if (asteroid.collidesWith(bullet)) {
                        if (asteroid.getScale() > 1) {
                            newAsteroidsList.addAll(asteroid.split());
                        }
                        effectsList.addAll(makeExplosionEffects(asteroid.getPosition()));
                        asteroidIterator.remove();
                        bulletIterator.remove();
                        score += 100;
                        break;
                    }
                }
            }
            asteroidList.addAll(newAsteroidsList);

            // Handles bullet movement and removal after going off-screen
            for (Iterator<Bullet> bulletIterator = bulletList.iterator(); bulletIterator.hasNext(); ) {
                Bullet bullet = bulletIterator.next();
                if (!bullet.move(width, height)) {
                    bulletIterator.remove();
                }
                bullet.paint(brush);
            }

            // Ship movements & respawning
            ship.updateTimers();
            if (ship.isAlive()) {
                ship.move(width, height);
                ship.rotate();
                ship.paint(brush);
            } else {
                if (ship.getRespawnTimer() == 0) {
                 polygonEffectsList.clear();
                }
            }

            // Draw invulnerability meter
            if (ship.isInvulnerable()) {
                invulnerableBox.paint(brush);
                brush.setColor(Color.yellow);
                brush.fillRect(10, (int) (100 + ((400 - ship.getInvulnerableTimer()) / 2.66666667)), 31, (int) (151 - (400 - ship.getInvulnerableTimer()) / 2.66666667));
            }

            // Draw dead ship effects
            for (Polygon polygon: polygonEffectsList) {
                polygon.move(width, height);
                brush.setColor(white);
                brush.drawLine((int) polygon.getTransformedPoints()[0].getX(), (int) polygon.getTransformedPoints()[0].getY(), (int) polygon.getTransformedPoints()[1].getX(), (int) polygon.getTransformedPoints()[1].getY());
            }

            // Level up
            if (asteroidList.isEmpty()) {
                level++;
                generateAsteroidsForLevel(level);
                score += 300;
            }

            // Move & draw effects
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

    public Point generateLocation() {
        return new Point(Math.random() * width, Math.random() * height);
    }

    private void generateAsteroidsForLevel(int level) {
        Point spawnBoxPosition = new Point(ship.position.getX() - (width / 6.0), ship.getPosition().getY() - (height / 6.0));
        spawnBox.setPosition(spawnBoxPosition);
        asteroidList.clear();
        int asteroidsNumber = (level * 2) + 2;
        for (int i = 0; i < asteroidsNumber; i++) {
            Asteroid asteroid = new Asteroid(2, generateLocation(), new Velocity(Math.random() * 360, 0.9));
            if (asteroid.collidesWith(spawnBox)) {
                i--;
            } else {
                asteroidList.add(asteroid);
            }
        }
    }

    public boolean spawnBoxIsEmpty() {
        for (Asteroid asteroid : asteroidList) {
            if (asteroid.collidesWith(spawnBox)) {
                return false;
            }
        }
        return true;
    }

    public List<Effect> makeExplosionEffects(Point position) {
        List<Effect> newEffects = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            double direction = i * (36 + Math.random() * 5);
            Effect effect = new Effect(1, position, new Velocity(direction, 1.2), 50);
            effect.color = white;
            newEffects.add(effect);
        }
        return newEffects;
    }

    public void resetLives(int maxLives) {
        livesList.clear();
        for (int i = 0; i < maxLives; i++) {
            Ship spareShip = new Ship(new Point(10 + (i * 35), 10), 0.7, new Velocity(0, 0));
            spareShip.color = white;
            livesList.add(spareShip);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // if (!ship.isAlive() && ship.getRespawnTimer() == 0 && spawnBoxIsEmpty()) {
        if (!ship.isAlive() && ship.getRespawnTimer() == 0){
            ship.respawn(new Point (width/2.0,height/2.0));
        }
        else if (e.getKeyCode() == 38 || e.getKeyChar() == 'w') { //up arrow
            ship.setEngineOn(true);
        } else if (e.getKeyCode() == 37 || e.getKeyChar() == 'a') { //left arrow
            ship.setRotationSpeed(-2.5);
        } else if (e.getKeyCode() == 39 || e.getKeyChar() == 'd') { //right arrow
            ship.setRotationSpeed(2.5);
        } else if (ship.isAlive() && e.getKeyCode() == KeyEvent.VK_SPACE) {
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
            polygonEffectsList.clear();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
//        else if (e.getKeyChar() == 'k') {
//            polygonEffectsList.addAll(ship.die());
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 38 || e.getKeyChar() == 'w') { // up arrow
            ship.setEngineOn(false);
        } else if (e.getKeyCode() == 37 || e.getKeyChar() == 'a') { // left arrow
            ship.setRotationSpeed(0);
        } else if (e.getKeyCode() == 39 || e.getKeyChar() == 'd') { // right arrow
            ship.setRotationSpeed(0);
        }
    }
}