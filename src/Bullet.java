import java.awt.*;

public class Bullet extends Circle {

    public Bullet(Point position, Velocity velocity) {
        super(2, position, velocity);
        color = Color.red;
    }

    @Override
    public boolean move(int width, int height) {
        Point newPosition = new Point(position.getX() + (velocity.getSpeed() * Math.cos(Math.toRadians(velocity.getDirection()))), position.getY() + (velocity.getSpeed() * Math.sin(Math.toRadians(velocity.getDirection()))));
        setPosition(newPosition);

        if (position.getX() < -10 || position.getX() > width+10) {
            return false;
        }
        if (position.getY() < -10 || position.getY() > height+10) {
            return false;
        }
        return true;
    }
}
