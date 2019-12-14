import java.awt.*;

public class Bullet extends Circle {

    public Bullet(Point position, double rotation) {
        super(position, 10, rotation);
        speed = 6;
        color = Color.red;
    }

    public boolean move(int width, int height) {
        Point newPosition = new Point(position.getX() + (speed * Math.cos(Math.toRadians(rotation))), position.getY() + (speed * Math.sin(Math.toRadians(rotation))));
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
