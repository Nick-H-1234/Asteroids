public class Asteroid extends Polygon {

    public static final Point[] ASTEROID_SHAPE_1 = {new Point(20, 0), new Point(40, 0), new Point(60, 20), new Point(40, 40), new Point(20, 40), new Point(0, 20)};
    public static final Point[] ASTEROID_SHAPE_2 = {new Point(10, 0), new Point(20, 0), new Point(10, 10), new Point(20, 20), new Point(10, 20), new Point(0, 10)};
    public static final Point[] ASTEROID_SHAPE_TEST = {new Point(0, 0), new Point(100, 0), new Point(100, 100), new Point(0, 100)};

    public Asteroid(Point[] shape, Point position, double rotation, double scale) {
        super (shape, position, rotation, scale);
        setSpeed(1);
    }
}