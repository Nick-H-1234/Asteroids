import org.junit.Assert;
import org.junit.Test;

public class CircleTest {

    @Test
    public void getRadius_1() {
        Circle circle = new Circle(1, new Point(0, 0), new Velocity (0,0));
        Assert.assertEquals(1, circle.getRadius());
    }
}
