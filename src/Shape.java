import java.awt.*;

public abstract class Shape {
    protected double speed = 0;
    protected double rotationSpeed = 0;
    protected Point position;   // The offset mentioned above.
    protected double rotation; // Zero degrees is due east.
    protected Color color = Color.white;

    public Shape(Point position, double rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Point getPosition() {
        return position;
    }
    public void setPosition(Point position) {
        this.position = position;
    }

    public double getRotation() {
        return this.rotation;
    }
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setRotationSpeed(double rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

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
    protected abstract Point findCenter();

    public abstract void paint(Graphics brush);

    /**
     * Move this shape according to speed and rotation within a box of dimensions width and height.
     * @param width the width of the frame.
     * @param height the height of the frame.
     * @return true if the shape is still active after the move.
     */
    public boolean move(int width, int height) {
        double x = position.getX() + (speed * Math.cos(Math.toRadians(rotation)));
        double y = position.getY() + (speed * Math.sin(Math.toRadians(rotation)));

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

        Point newPosition = new Point((int) x, (int) y);
        setPosition(newPosition);
        return true;
    }

    public void rotate() {
        rotation += rotationSpeed;
    }

}
