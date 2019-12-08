import java.awt.*;

public class Bullet extends Circle {

    public Bullet(Point position, double rotation) {
        super(position, 2, rotation);
        speed = 6;
        color = Color.red;
    }

    public boolean move(int width, int height) {
        position.x += (speed * Math.cos(Math.toRadians(rotation)));
        position.y += (speed * Math.sin(Math.toRadians(rotation)));

        if (position.x < -10 || position.x > width+10) {
            return false;
        }
        if (position.y < -10 || position.y > height+10) {
            return false;
        }
        return true;
    }
}
