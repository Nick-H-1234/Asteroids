import org.junit.Assert;
import org.junit.Test;

public class VelocityTest {

    @Test
    public void testVectorAddition() {
        Velocity velocity1 = new Velocity(30,1);
        Velocity velocity2 = new Velocity(90,2);
        Velocity velocity3 = new Velocity(70.9,2.65);
        Velocity result = velocity1.add(velocity2);
        Assert.assertEquals(new Velocity(70.89339464913091,2.6457513110645907), result);
    }

    @Test
    public void testVector2() {
        Velocity velocity1 = new Velocity(30,1);
        Velocity velocity2 = new Velocity(210,2);
        Velocity velocity3 = new Velocity(210,1);
        Assert.assertEquals(velocity3, velocity1.add(velocity2));
    }

    @Test
    public void directionWithinTolerance() {
        Velocity velocity1 = new Velocity(10,10);
        Velocity velocity2 = new Velocity(10+0.1/1E12,10);
        Assert.assertEquals(velocity1,velocity2);
        Assert.assertEquals(velocity1.hashCode(),velocity2.hashCode());
    }

    @Test
    public void directionOutsideTolerance() {
        Velocity velocity1 = new Velocity(10,10);
        Velocity velocity2 = new Velocity(10+0.5/1E12,10);
        Assert.assertNotEquals(velocity1,velocity2);
        Assert.assertNotEquals(velocity1.hashCode(),velocity2.hashCode());
    }

    @Test
    public void speedWithinTolerance() {
        Velocity velocity1 = new Velocity(10,10);
        Velocity velocity2 = new Velocity(10+0.1/1E12,10+0.1/1E12);
        Assert.assertEquals(velocity1,velocity2);
        Assert.assertEquals(velocity1.hashCode(),velocity2.hashCode());
    }

    @Test
    public void speedOutsideTolerance() {
        Velocity velocity1 = new Velocity(10,10);
        Velocity velocity2 = new Velocity(10,10+1E-10);
        Assert.assertNotEquals(velocity1,velocity2);
        Assert.assertNotEquals(velocity1.hashCode(),velocity2.hashCode());
    }
}
