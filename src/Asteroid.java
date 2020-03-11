import java.util.ArrayList;
import java.util.List;

public class Asteroid extends Polygon {

    private static final Point[] ASTEROID_SHAPE_1 = {new Point(19,11),new Point(36,11),new Point(51,25),new Point(50,48),new Point(30,62),new Point(17,61),new Point(21,45),new Point(10,56),new Point(1,39),new Point(18,37), new Point(4,26)};
    private static final Point[] ASTEROID_SHAPE_2 = {new Point(86, 13), new Point(93, 22), new Point(82, 22), new Point(75, 16), new Point(79, 38), new Point(83, 30),new Point(93, 30),new Point(81, 50),new Point(102, 55),new Point(122, 44),new Point(122, 25),new Point(110, 13),};

    public Asteroid(double scale, Point position, Velocity velocity) {
        super (Math.random() < 0.5 ? ASTEROID_SHAPE_1 : ASTEROID_SHAPE_2, scale, position, Math.random() * 360, (Math.random() < 0.5 ? 1 : -1)*Math.random(), velocity);
    }

    public List<Asteroid> split() {
        Point point = getPosition();
        double direction = velocity.getDirection();
        double speed = velocity.getSpeed();
        List<Asteroid> newAsteroidsList = new ArrayList<>();
        newAsteroidsList.add(new Asteroid(getScale() - 0.5, point, new Velocity(direction + Math.random() * 30, speed + Math.random()*0.1 + 0.2)));
        newAsteroidsList.add(new Asteroid(getScale() - 0.5, point, new Velocity(direction - Math.random() * 30, speed + Math.random()*0.1 + 0.2)));
        return newAsteroidsList;
    }
}