import java.awt.*;

public abstract class Shape {
    protected double speed = 0;
    protected int rotationSpeed = 0;
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


    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public abstract boolean collidesWith(Shape other);

    protected double distanceBetween(Point n, Point m) {
        double a = n.x - m.x;
        double b = n.y - m.y;
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
        position.x += (speed * Math.cos(Math.toRadians(rotation)));
        position.y += (speed * Math.sin(Math.toRadians(rotation)));

        if (position.x > width) {
            position.x = 0;
        } else if (position.x < 0) {
            position.x = width;
        }
        if (position.y > height) {
            position.y = 0;
        } else if (position.y < 0) {
            position.y = height;
        }
        return true;
    }

    public void rotate() {
        rotation += rotationSpeed;
    }

    public void setRotationSpeed(int rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }
}
