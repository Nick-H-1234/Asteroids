import org.junit.Assert;
import org.junit.Test;

public class VelocityTest {

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
