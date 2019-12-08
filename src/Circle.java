import java.awt.*;

public class Circle extends Shape {
    private int radius;

    public Circle(Point position, int radius, double rotation) {
        super(position, rotation);
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public boolean collidesWith(Shape other) {
        if (other instanceof Polygon) {
            Point[] otherPolygon = ((Polygon) other).getTransformedPoints();
            for (int i = 0; i < otherPolygon.length; i++) {
                double distance = distanceBetween(otherPolygon[i], position);

                if (distance <= radius) {
                    return true;
                }
            }
        }
        else if (other instanceof Circle) {
            Circle circle = (Circle) other;
            double distance = distanceBetween(position,circle.position);
            if (distance <= radius + circle.getRadius()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Point point) {
        double distance = distanceBetween(position, point);
        return distance <= radius;
    }

    @Override
    protected Point findCenter() {
        return position;
    }

    public void paint(Graphics brush) {
        brush.setColor(color);
        int x = this.position.x;
        int y = this.position.y;
        brush.fillOval(x,y,radius*2,radius*2);
    }


}
