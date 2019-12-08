import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CircleTest {

    @Test
    public void getRadius_1() {
        Circle circle = new Circle(new Point(0, 0), 1, 0);
        Assert.assertEquals(1, circle.getRadius());
    }
}
