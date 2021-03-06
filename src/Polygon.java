/*
CLASS: Polygon
DESCRIPTION: A polygon is a sequence of points in space defined by a set of
             such points, an offset, and a rotation. The offset is the
             distance between the origin and the center of the shape.
             The rotation is measured in degrees, 0-360.
USAGE: You are intended to instantiate this class with a set of points that
       forever defines its shape, and then modify it by repositioning and
       rotating that shape. In defining the shape, the relative positions
       of the points you provide are used, in other words: {(0,1),(1,1),(1,0)}
       is the same shape as {(9,10),(10,10),(10,9)}.
NOTE: You don't need to worry about the "magic math" details.
*/

import java.awt.*;

class Polygon extends Shape {
    private Point[] shape;   // An array of points.
    private double scale;
    private double rotation;
    private double rotationSpeed;

    /**
     * Creates a polygon based at position, where the shape is defined in shape, as displacements from position.
     * shape is pre-processed to be as close as possible to the origin.
     * @param shape displacements from position which defines the shape in x, y coordinates
     * @param scale the scale of the shape
     * @param position the reference point of the shape in x, y coordinates
     * @param rotation the direction the polygon is facing in degrees from due east
     * @param rotationSpeed the speed at which the polygon will rotate
     * @param velocity the velocity of the polygon
     */
    public Polygon(Point[] shape, double scale, Point position, double rotation, double rotationSpeed, Velocity velocity) {
        super(position, velocity);
        this.shape = shape;
        this.scale = scale;
        this.rotation = rotation;
        this.rotationSpeed = rotationSpeed;

        // First, we find the shape's top-most left-most boundary, its origin.
        double minx = this.shape[0].getX();
        double miny = this.shape[0].getY();
        for (Point p : this.shape) {
            minx = Math.min(minx, p.getX());
            miny = Math.min(miny, p.getY());
        }

        // Then, we orient all of its points relative to the real origin.
        for (int i = 0; i < this.shape.length; i++) {
            Point point = new Point(this.shape[i].getX()-minx, this.shape[i].getY()-miny);
            this.shape[i] = point;
        }
    }

    public Point[] getTransformedPoints() {
        Point center = findCentre();
        Point[] points = new Point[shape.length];
        for (int i = 0; i < shape.length; i++) {
            Point p = shape[i];
            double x = (((p.getX() - center.getX()) * Math.cos(Math.toRadians(rotation))
                    - (p.getY() - center.getY()) * Math.sin(Math.toRadians(rotation))) * scale)
                    + center.getX() + position.getX();
            double y = (((p.getX() - center.getX()) * Math.sin(Math.toRadians(rotation))
                    + (p.getY() - center.getY()) * Math.cos(Math.toRadians(rotation))) * scale)
                    + center.getY() + position.getY();
            points[i] = new Point(x, y);
        }
        return points;
    }

    @Override
    public void paint(Graphics brush) {
        brush.setColor(color);
        Point[] transformedPoints = getTransformedPoints();
        int[] xValues = new int[transformedPoints.length];
        int[] yValues = new int[transformedPoints.length];
        for (int i = 0; i < transformedPoints.length; i++) {
            xValues[i] = (int) transformedPoints[i].getX();
            yValues[i] = (int) transformedPoints[i].getY();
        }
        brush.drawPolygon(xValues, yValues, transformedPoints.length);
    }

    @Override
    public boolean collidesWith(Shape other) {
        if (other instanceof Polygon) {
            Polygon polygon = (Polygon) other;
            Point[] polygonTransformedPoints = polygon.getTransformedPoints();
            for (int i =0; i < polygon.shape.length ; i++) {
                if (contains(polygonTransformedPoints[i])) {
                    return true;
                }
            }
            Point[] transformedPoints = getTransformedPoints();
            for (int i =0; i < this.shape.length ; i++) {
                if (polygon.contains(transformedPoints[i])) {
                    return true;
                }
            }
        }
        else if (other instanceof Circle) {
            Circle circle = (Circle) other;
            if (this.contains(circle.getPosition())) {
                return true;
            }
            // Make a polygon that approximates the circle
            Point[] approximationPoints = new Point[8];
            for (int i = 0; i < 8;i++) {
                approximationPoints[i] = makePointOnCircle(circle.findCentre(), i*45, circle.getRadius());
            }
            Point circleCenter = circle.findCentre();
            Point polygonPosition = new Point(circleCenter.getX() -circle.getRadius(), circleCenter.getY() -circle.getRadius());
            Polygon approximation = new Polygon(approximationPoints, 1, polygonPosition, 0, 0, new Velocity(0,0));
            return collidesWith(approximation);
        }
        return false;
    }

    @Override
    public boolean contains(Point point) {
        Point[] points = getTransformedPoints();
        double crossingNumber = 0;
        for (int i = 0, j = 1; i < shape.length; i++, j = (j + 1) % shape.length) {
            if ((((points[i].getX() < point.getX()) && (point.getX() <= points[j].getX())) ||
                    ((points[j].getX() < point.getX()) && (point.getX() <= points[i].getX()))) &&
                    (point.getY() > points[i].getY() + (points[j].getY() - points[i].getY()) /
                            (points[j].getX() - points[i].getX()) * (point.getX() - points[i].getX()))) {
                crossingNumber++;
            }
        }
        return crossingNumber % 2 == 1;
    }

    private double findArea() {
        double sum = 0;
        for (int i = 0, j = 1; i < shape.length; i++, j = (j + 1) % shape.length) {
            sum += shape[i].getX() * shape[j].getY() - shape[j].getX() * shape[i].getY();
        }
        return Math.abs(sum / 2);
    }

    @Override
    protected Point findCentre() {
        double x = 0 , y = 0;
        for (int i = 0, j = 1; i < shape.length; i++, j = (j + 1) % shape.length) {
            x += (shape[i].getX() + shape[j].getX())
                    * (shape[i].getX() * shape[j].getY() - shape[j].getX() * shape[i].getY());
            y += (shape[i].getY() + shape[j].getY())
                    * (shape[i].getX() * shape[j].getY() - shape[j].getX() * shape[i].getY());
        }
        double area = findArea();
        return new Point(Math.abs(x / (6 * area)), Math.abs(y / (6 * area)));
    }

    public Point makePointOnCircle(Point position, double angle, int radius) {
        double x = radius * Math.cos(Math.toRadians(angle))
                + position.getX();
        double y = radius * Math.sin(Math.toRadians(angle))
                + position.getY();
        return new Point (x,y);
    }

    public void rotate() {
        rotation += rotationSpeed;
    }

    public double getScale() {
        return scale;
    }

    public double getRotation() {return rotation;}
    public void setRotation(double rotation) {this.rotation = rotation;}

    public void setRotationSpeed(double rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }
}