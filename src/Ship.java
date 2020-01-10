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
    protected int timer;
    int bulletCooldown;


    public Ship(Point position, double scale, Velocity velocity) {
        super(SHIP_SHAPE, scale, position,-90, 0, velocity);
    }

    public void reset(Point position) {
        velocity.setSpeed(0);
        setRotation(-90);
        this.position = position;
        setAlive(true);
        setInvulnerable(false);
    }

    public boolean isInvulnerable() {return invulnerable;}

    public void setInvulnerable(boolean invulnerable) {this.invulnerable = invulnerable;}

    public boolean isAlive() {return alive;}

    public void setAlive(boolean alive) {this.alive = alive;}

    public void setRotation(double rotation) {this.rotation = rotation;}

    public boolean move(int width, int height) {
        if (velocity.getSpeed() > 0.05) {
            velocity.setSpeed(velocity.getSpeed()-DRAG);
        }
        else if (velocity.getSpeed() < 0.05 && !engineOn) {
            velocity.setSpeed(0);
        }


        if (engineOn) {
            Velocity newVelocity = new Velocity(getRotation(), ACCELERATION - (velocity.getSpeed()/100));
            velocity = velocity.add(newVelocity);
        }


        velocity.setSpeed(Math.min(velocity.getSpeed(), MAX_SPEED));

        double x = position.getX() + (velocity.getSpeed() * Math.cos(Math.toRadians(velocity.getDirection())));
        double y = position.getY() + (velocity.getSpeed() * Math.sin(Math.toRadians(velocity.getDirection())));

        if (x < 0) {x = width;}
        if (x > width) {x = 0;}

        if (y < 0) {y = height;}
        if (y > height) {y = 0;}

        Point newPosition = new Point(x,y);
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


    public void shipTick() {
        if (timer > 0) {
            timer--;
            if (timer == 0) {
                setInvulnerable(false);
            }
        }
       if (bulletCooldown > 0 ) {
           bulletCooldown--;
       }
    }
}