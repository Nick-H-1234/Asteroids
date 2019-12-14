import org.junit.Assert;
import org.junit.Test;

import static java.lang.Math.sqrt;

public class ShapeTest {
    public static final Point[] SQUARE100_POINTS = {new Point(0, 0), new Point(100, 0), new Point(100, 100), new Point(0, 100)};
    public static final Point[] SQUARE100_POINTS_40_50 = {new Point(40, 50), new Point(140, 50), new Point(140, 150), new Point(40, 150)};
    public static final Point[] SQUARE100_POINTS_30 = {new Point(31.698729810778058, -18.30127018922194), new Point(118.30127018922194, 31.698729810778058), new Point(68.30127018922194, 118.30127018922194), new Point(-18.30127018922194, 68.30127018922194)};
    public static final Point[] SQUARE100_POINTS_40_50_30 = {new Point(71.69872981077806, 31.69872981077806), new Point(158.30127018922195, 81.69872981077806), new Point(108.30127018922194, 168.30127018922195), new Point(21.69872981077806, 118.30127018922194)};

    public static final Point[] SQUARE200_POINTS = {new Point(-50, -50), new Point(150, -50), new Point(150, 150), new Point(-50, 150)};
    public static final Point[] SQUARE150_POINTS = {new Point(-25, -25), new Point(125, -25), new Point(125, 125), new Point(-25, 125)};

    // Polygon vs Polygon collisions
    @Test
    public void shipOutsideSquare() {
        Ship ship = new Ship(new Point (200,0),0);
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (0,0),0,1 );
        Assert.assertFalse(ship.collidesWith(square));
        Assert.assertFalse(square.collidesWith(ship));
    }

    @Test
    public void shipInsideSquare() {
        Ship ship = new Ship(new Point (20,20),0);
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (0,0),0, 1);
        Assert.assertTrue(square.collidesWith(ship));
        Assert.assertTrue(ship.collidesWith(square));
    }

    @Test
    public void shipOverlapsSideOfSquare() {
        Ship ship = new Ship(new Point (90,50),0);
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (0,0),0, 1);
        Assert.assertTrue(ship.collidesWith(square));
        Assert.assertTrue(square.collidesWith(ship));
    }

    @Test
    public void shipOverlapsCornerOfSquare() {
        Ship ship = new Ship(new Point (99,90),0);
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (50,50),0,1 );
        Assert.assertTrue(ship.collidesWith(square));
        Assert.assertTrue(square.collidesWith(ship));
    }


    // Find centre of Polygon
    @Test
    public void findCentreOfSquare() {
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (100,100),0, 1);
        Assert.assertEquals(new Point(50,50),square.findCenter());
    }

    @Test
    public void findCentreOfSquare2() {
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (0,0),0, 1);
        Assert.assertEquals(new Point(50,50),square.findCenter());
    }

    @Test
    public void findCentreOfSquare3() {
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (200,250),95,1 );
        Assert.assertEquals(new Point(50,50),square.findCenter());
    }


    //Test getTranformedPoints()
    @Test
    public void testTransform() {
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (0,0),0, 1);
        Point[] result = square.getTransformedPoints();
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
        Assert.assertArrayEquals(SQUARE100_POINTS, result);
    }

    @Test
    public void testTransform40_50() {
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (40,50),0, 1);
        Point[] result = square.getTransformedPoints();
        Assert.assertArrayEquals(SQUARE100_POINTS_40_50, result);
    }

    @Test
    public void testTransform0_0_30() {
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (0,0),30, 1);
        Point[] result = square.getTransformedPoints();
        Assert.assertArrayEquals(SQUARE100_POINTS_30, result);
    }

    @Test
    public void testTransform40_50_30() {
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (40,50),30, 1);
        Point[] result = square.getTransformedPoints();
        Assert.assertArrayEquals(SQUARE100_POINTS_40_50_30, result);
    }


    //Test Polygon vs Circle collisions
    @Test
    public void circleOutsideSquare() {
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (0,0),0, 1);
        Circle circle = new Circle(new Point(200,0),5,0);
        Assert.assertFalse(square.collidesWith(circle));
    }

    @Test
    public void circleInsideSquare() {
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (0,0),0, 1);
        Circle circle = new Circle(new Point(50,50),5,0);
        Assert.assertTrue(square.collidesWith(circle));
    }

    @Test
    public void circleOverlapsSquare() {
        Polygon square = new Polygon(SQUARE100_POINTS,new Point (0,0),0, 1);
        Circle circle = new Circle(new Point(102,50),5,0);
        Assert.assertTrue(square.collidesWith(circle));
    }


    @Test
    public void testMakeCirclePoints() {
        Polygon polygon = new Polygon(SQUARE100_POINTS,new Point(0,0), 0, 1);
        Assert.assertEquals(new Point(5,0), polygon.makePointOnCircle(new Point(0,0), 0, 5));
        Assert.assertEquals(new Point(0,5), polygon.makePointOnCircle(new Point(0,0), 90, 5));
        Assert.assertEquals(new Point( -5 / sqrt(2), -5 / sqrt(2)), polygon.makePointOnCircle(new Point(0,0), 225, 5));
    }

    @Test
    public void testMakeTranslatedCirclePoints() {
        Polygon polygon = new Polygon(SQUARE100_POINTS,new Point(0,0), 0,1 );
        Assert.assertEquals(new Point(15,20), polygon.makePointOnCircle(new Point(10,20), 0, 5));
        Assert.assertEquals(new Point(10,25), polygon.makePointOnCircle(new Point(10,20), 90, 5));
        Assert.assertEquals(new Point (10+(-5 / sqrt(2)), 20+(-5 / sqrt(2))), polygon.makePointOnCircle(new Point(10,20), 225, 5));
    }

    @Test
    public void testScale2() {
        Asteroid asteroid = new Asteroid(SQUARE100_POINTS,new Point(0,0), 0, 2);
        Point[] result = asteroid.getTransformedPoints();
        Assert.assertArrayEquals(SQUARE200_POINTS,result);
    }

    @Test
    public void testScale1Point5() {
        Asteroid asteroid = new Asteroid(SQUARE100_POINTS,new Point(0,0), 0, 1.5);
        Point[] result = asteroid.getTransformedPoints();
        Assert.assertArrayEquals(SQUARE150_POINTS,result);
    }
}