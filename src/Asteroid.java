public class Asteroid extends Polygon {

    public static final Point[] ASTEROID_SHAPE_1 = {new Point(20, 0), new Point(40, 0), new Point(60, 20), new Point(40, 40), new Point(20, 40), new Point(0, 20)};
    public static final Point[] ASTEROID_SHAPE_2 = {new Point(10, 0), new Point(20, 0), new Point(10, 10), new Point(20, 20), new Point(10, 20), new Point(0, 10)};
    public static final Point[] ASTEROID_SHAPE_TEST = {new Point(0, 0), new Point(100, 0), new Point(100, 100), new Point(0, 100)};
    public static final Point[] ASTEROID_SHAPE_3 = {new Point(19,11),new Point(36,11),new Point(51,25),new Point(50,48),new Point(30,62),new Point(17,61),new Point(21,45),new Point(10,56),new Point(1,39),new Point(18,37), new Point(4,26)};
    public static final Point[] ASTEROID_SHAPE_4 = {new Point(86, 13), new Point(93, 22), new Point(82, 22), new Point(75, 16), new Point(79, 38), new Point(83, 30),new Point(93, 30),new Point(81, 50),new Point(102, 55),new Point(122, 44),new Point(122, 25),new Point(110, 13),};

    public Asteroid(Point[] shape, Point position, double rotation, double scale) {
        super (shape, position, rotation, scale);
        setSpeed(1);
    }
}