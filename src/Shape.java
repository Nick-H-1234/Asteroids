import java.awt.*;
import java.beans.VetoableChangeListener;

public abstract class Shape {
    protected Point position;
    protected Velocity velocity;
    protected Color color = Color.white;

    public Shape(Point position, Velocity velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public Point getPosition() {
        return position;
    }
    public void setPosition(Point position) {
        this.position = position;
    }

    public abstract boolean collidesWith(Shape other);

    protected double distanceBetween(Point n, Point m) {
        double a = n.getX() - m.getX();
        double b = n.getY() - m.getY();
        return Math.sqrt((Math.pow(a,2))+(Math.pow(b,2)));
    }

    public abstract boolean contains(Point point);

    /**
     * Locate the centre of this shape before any translation or rotation.
     * @return Point: The centre point.
     */
    protected abstract Point findCentre();

    public abstract void paint(Graphics brush);

    /**
     * Move this shape according to speed and rotation within a box of dimensions width and height.
     * @param width the width of the frame.
     * @param height the height of the frame.
     * @return true if the shape is still active after the move.
     */
    public boolean move(int width, int height) {
        double x = position.getX() + (velocity.getSpeed() * Math.cos(Math.toRadians(velocity.getDirection())));
        double y = position.getY() + (velocity.getSpeed() * Math.sin(Math.toRadians(velocity.getDirection())));

        if (x > width) {
            x = 0;
        } else if (x < 0) {
            x = width;
        }
        if (y > height) {
            y = 0;
        } else if (y < 0) {
            y = height;
        }

        Point newPosition = new Point(x,y);
        setPosition(newPosition);
        return true;
    }
}