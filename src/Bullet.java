import java.awt.*;

public class Bullet extends Circle {

    public Bullet(Point position, double direction, double speed) {
        super(2, position, direction, speed);
        color = Color.red;
    }

    @Override
    public boolean move(int width, int height) {
        Point newPosition = new Point(position.getX() + (speed * Math.cos(Math.toRadians(direction))), position.getY() + (speed * Math.sin(Math.toRadians(direction))));
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
