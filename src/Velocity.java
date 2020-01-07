public class Velocity {
    public static final double tolerance = 1E-12;
    private double speed;
    private double direction;

    public Velocity(double direction, double speed) {
        this.direction = direction;
        this.speed = speed;
    }

    public double getSpeed() {return speed;}
    public void setSpeed(double speed) {this.speed = speed;}

    public double getDirection() {return direction;}
    public void setDirection(double direction) {this.direction = direction;}

    public static Velocity add(Velocity velocity1, Velocity velocity2) {
        double x1 = Math.cos(Math.toRadians(velocity1.getDirection()))*velocity1.getSpeed();
        double y1 = Math.sin(Math.toRadians(velocity1.getDirection()))*velocity1.getSpeed();

        double x2 = Math.cos(Math.toRadians(velocity2.getDirection()))*velocity2.getSpeed();
        double y2 = Math.sin(Math.toRadians(velocity2.getDirection()))*velocity2.getSpeed();

        double x3 = x1 + x2;
        double y3 = y1 + y2;

//        double theta = Math.atan((y1+y2)/(x1+x2));
        double theta = Math.atan2(y3,x3);
        double thetaDegrees = Math.toDegrees(theta);
        thetaDegrees = (thetaDegrees + 360) % 360;

        //double speed = (y1+y2)/Math.sin(theta);
        double speed = Math.sqrt(Math.pow((y1+y2),2)+Math.pow((x1+x2),2));

//        double newSpeed = Math.sqrt(Math.pow(velocity1.getSpeed(),2)+Math.pow(velocity2.getSpeed(),2)+2*velocity1.getSpeed()*velocity2.getSpeed()*(Math.cos(Math.toRadians(velocity2.getDirection())-Math.toRadians(velocity1.getDirection()))));
//        double newTheta = velocity1.getDirection() + Math.atan2(velocity2.getSpeed()*Math.sin(Math.toRadians(velocity2.getDirection()-velocity1.getDirection())),velocity1.getSpeed()+velocity2.getSpeed()*Math.cos(Math.toRadians(velocity2.getDirection()-velocity1.getDirection())));

        Velocity result = new Velocity(thetaDegrees,speed);
        Velocity test = new Velocity(90,5);
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Velocity)) {
            return false;
        }
        Velocity otherVelocity = (Velocity) other;
        return Math.abs(otherVelocity.getDirection() - this.getDirection()) < tolerance && Math.abs(otherVelocity.getSpeed() - otherVelocity.getSpeed()) < tolerance;
    }

    @Override
    public String toString() {
        return "Direction: " + direction + ", Speed: " + speed;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
