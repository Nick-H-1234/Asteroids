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
    protected double scale = 2;

    /**
     * Creates a polygon based at position, where the shape is defined in shape, as displacements from position.
     * @param shape displacements from position which defines the shape in x, y coordinates
     * @param position the reference point of the shape in x, y coordinates
     * @param rotation the rotation of the shape in degrees
     * @param scale the scale of the shape
     */
    public Polygon(Point[] shape, Point position, double rotation, double scale) {
        super(position, rotation);
        this.shape = shape;
        this.scale = scale;

        // First, we find the shape's top-most left-most boundary, its origin.
        Point origin = null;
        try {
            origin = (Point) this.shape[0].clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        for (Point p : this.shape) {
            if (p.x < origin.x) origin.x = p.x;
            if (p.y < origin.y) origin.y = p.y;
        }

        // Then, we orient all of its points relative to the real origin.
        for (Point p : this.shape) {
            p.x -= origin.x;
            p.y -= origin.y;
        }
    }

    // "getPoints" applies the rotation and offset to the shape of the polygon.
    public Point[] getTransformedPoints() {
        Point center = findCenter();
        Point[] points = new Point[shape.length];
        for (int i = 0; i < shape.length; i++) {
            Point p = shape[i];
            int x = (int) ((((p.x - center.x) * Math.cos(Math.toRadians(rotation))
                    - (p.y - center.y) * Math.sin(Math.toRadians(rotation))) * scale)
                    + center.x + position.x);
            int y = (int) ((((p.x - center.x) * Math.sin(Math.toRadians(rotation))
                    + (p.y - center.y) * Math.cos(Math.toRadians(rotation))) * scale)
                    + center.y + position.y);
            points[i] = new Point(x, y);
        }
        return points;
    }

    public Point makePointOnCircle(Point position, double angle, int radius) {
        int x = (int) (radius * Math.cos(Math.toRadians(angle))
                + position.x);
        int y = (int) (radius * Math.sin(Math.toRadians(angle))
                 + position.y);
        Point point = new Point (x,y);
        return point;
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
                approximationPoints[i] = makePointOnCircle(circle.findCenter(), i*45, circle.getRadius());
            }
            Point circleCenter = circle.findCenter();
            Point polygonPosition = new Point(circleCenter.x-circle.getRadius(),circleCenter.y-circle.getRadius());
            Polygon approximation = new Polygon(approximationPoints, polygonPosition,0, 1);
            return collidesWith(approximation);
        }
        return false;
    }

    // "contains" implements some magical math (i.e. the ray-casting algorithm).
    @Override
    public boolean contains(Point point) {
        Point[] points = getTransformedPoints();
        double crossingNumber = 0;
        for (int i = 0, j = 1; i < shape.length; i++, j = (j + 1) % shape.length) {
            if ((((points[i].x < point.x) && (point.x <= points[j].x)) ||
                    ((points[j].x < point.x) && (point.x <= points[i].x))) &&
                    (point.y > points[i].y + (points[j].y - points[i].y) /
                            (points[j].x - points[i].x) * (point.x - points[i].x))) {
                crossingNumber++;
            }
        }
        return crossingNumber % 2 == 1;
    }

    //public void rotate(int degrees) {
    //    rotation = (rotation + degrees) % 360;
    //}
  
  /*
  The following methods are private access restricted because, as this access
  level always implies, they are intended for use only as helpers of the
  methods in this class that are not private. They can't be used anywhere else.
  */

    // "findArea" implements some more magic math.
    private double findArea() {
        double sum = 0;
        for (int i = 0, j = 1; i < shape.length; i++, j = (j + 1) % shape.length) {
            sum += shape[i].x * shape[j].y - shape[j].x * shape[i].y;
        }
        return Math.abs(sum / 2);
    }

    // "findCenter" implements another bit of math.


    @Override
    protected Point findCenter() {
        Point sum = new Point(0, 0);
        for (int i = 0, j = 1; i < shape.length; i++, j = (j + 1) % shape.length) {
            sum.x += (shape[i].x + shape[j].x)
                    * (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
            sum.y += (shape[i].y + shape[j].y)
                    * (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
        }
        double area = findArea();
        return new Point((int) Math.abs(sum.x / (6 * area)), (int) Math.abs(sum.y / (6 * area)));
    }

    @Override
    public void paint(Graphics brush) {
        brush.setColor(color);
        Point[] transformedPoints = getTransformedPoints();
        int[] xValues = new int[transformedPoints.length];
        int[] yValues = new int[transformedPoints.length];
        for (int i = 0; i < transformedPoints.length; i++) {
            xValues[i] = transformedPoints[i].x;
            yValues[i] = transformedPoints[i].y;
        }
        brush.drawPolygon(xValues, yValues, transformedPoints.length);
    }

}