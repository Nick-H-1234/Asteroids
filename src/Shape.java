import java.awt.*;
import java.beans.VetoableChangeListener;

public abstract class Shape {
    protected Point position;   // The offset mentioned above.
    protected Velocity velocity; // Zero degrees is due east (along x axis)
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

//    public double getDirection() { return this.direction; }
//    public void setDirection(double direction) {
//        this.direction = direction;
//    }

//    public void setSpeed(double speed) { this.speed = speed; }

    public abstract boolean collidesWith(Shape other);

    protected double distanceBetween(Point n, Point m) {
        double a = n.getX() - m.getX();
        double b = n.getY() - m.getY();
        double c = Math.sqrt((Math.pow(a,2))+(Math.pow(b,2)));
        return c;
    }

    // "contains" implements some magical math (i.e. the ray-casting algorithm).
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
