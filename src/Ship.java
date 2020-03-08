import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Ship extends Polygon {

    public static final Point[] SHIP_SHAPE = {new Point(0, 0), new Point(0, 40), new Point(60, 20)};
    private static final double MAX_SPEED = 3;
    private static final double DRAG = 0.006;
    private static final double ACCELERATION = 0.09;

    private boolean invulnerable = false;
    private boolean alive = true;
    boolean engineOn = false;
    protected int invulnerableTimer;
    protected int respawnTimer;
    int bulletCooldown;


    public Ship(Point position, double scale, Velocity velocity) {
        super(SHIP_SHAPE, scale, position, -90, 0, velocity);
    }

    public void reset(Point position) {
        velocity.setSpeed(0);
        setRotation(-90);
        this.position = position;
        setAlive(true);
        setInvulnerable(false);
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public boolean move(int width, int height) {
        if (velocity.getSpeed() > 0.05) {
            velocity.setSpeed(velocity.getSpeed() - DRAG);
        } else if (velocity.getSpeed() < 0.05 && !engineOn) {
            velocity.setSpeed(0);
        }


        if (engineOn) {
            Velocity newVelocity = new Velocity(getRotation(), ACCELERATION - (velocity.getSpeed() / 100));
            velocity = velocity.add(newVelocity);
        }


        velocity.setSpeed(Math.min(velocity.getSpeed(), MAX_SPEED));

        double x = position.getX() + (velocity.getSpeed() * Math.cos(Math.toRadians(velocity.getDirection())));
        double y = position.getY() + (velocity.getSpeed() * Math.sin(Math.toRadians(velocity.getDirection())));

        if (x < 0) {
            x = width;
        }
        if (x > width) {
            x = 0;
        }

        if (y < 0) {
            y = height;
        }
        if (y > height) {
            y = 0;
        }

        Point newPosition = new Point(x, y);
        setPosition(newPosition);

        return true;
    }

    public List<Bullet> shoot() {
        List<Bullet> newBulletList = new ArrayList<>(1);
        if (bulletCooldown == 0) {
            double rotation = getRotation();
            Point position = getTransformedPoints()[2];
            Bullet bullet = new Bullet(position, new Velocity(rotation, 6));
            newBulletList.add(bullet);
            bulletCooldown = 15;
        }
        return newBulletList;
    }

    public List<Polygon> die() {
        setAlive(false);
        respawnTimer = 200;
        List<Polygon> polygonList = new ArrayList<>(3);
        Point[] shipPoints = getTransformedPoints();

        Velocity explosionVelocity = new Velocity(velocity.getDirection(), velocity.getSpeed() / 6).add(new Velocity(getRotation(), 0.2));
        Polygon polygon1 = new Polygon(new Point[]{shipPoints[0], shipPoints[1], new Point(shipPoints[1].getX() + 1, shipPoints[1].getY() + 1)}, 1, new Point(Math.min(shipPoints[0].getX(), shipPoints[1].getX()), Math.min(shipPoints[0].getY(), shipPoints[1].getY())), 0, 0, explosionVelocity);
        Polygon polygon2 = new Polygon(new Point[]{shipPoints[1], shipPoints[2], new Point(shipPoints[2].getX() + 1, shipPoints[2].getY() + 1)}, 1, new Point(Math.min(shipPoints[1].getX(), shipPoints[2].getX()), Math.min(shipPoints[1].getY(), shipPoints[2].getY())), 0, 0, new Velocity(explosionVelocity.getDirection() - 30, explosionVelocity.getSpeed()));
        Polygon polygon3 = new Polygon(new Point[]{shipPoints[2], shipPoints[0], new Point(shipPoints[0].getX() + 1, shipPoints[0].getY() + 1)}, 1, new Point(Math.min(shipPoints[2].getX(), shipPoints[0].getX()), Math.min(shipPoints[2].getY(), shipPoints[0].getY())), 0, 0, new Velocity(explosionVelocity.getDirection() + 30, explosionVelocity.getSpeed()));
        polygonList.add(polygon1);
        polygonList.add(polygon2);
        polygonList.add(polygon3);
        return polygonList;
    }

    public boolean respawn(Point position) {
        if (respawnTimer == 0) {
            setAlive(true);
            reset(position);
            setInvulnerable(true);
            invulnerableTimer = 400;
            return true;
        }
        return false;
    }


    public void shipTick() {
        if (isInvulnerable()) {
            color = Color.yellow;
        } else color = Color.white;

        if (invulnerableTimer > 0) {
            invulnerableTimer--;
            if (invulnerableTimer == 0) {
                setInvulnerable(false);
            }
        }

        if (bulletCooldown > 0) {
            bulletCooldown--;
        }
        if (respawnTimer > 0) {
            respawnTimer--;
        }
    }
}