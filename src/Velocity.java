public class Velocity {
    private static final double PRECISION = 1E+12;
    private double direction;
    private double speed;

    public Velocity(double direction, double speed) {
        this.direction = direction;
        this.speed = speed;
    }

    public Velocity add(Velocity other) {
        double x1 = Math.cos(Math.toRadians(this.getDirection()))*this.getSpeed();
        double y1 = Math.sin(Math.toRadians(this.getDirection()))*this.getSpeed();

        double x2 = Math.cos(Math.toRadians(other.getDirection()))*other.getSpeed();
        double y2 = Math.sin(Math.toRadians(other.getDirection()))*other.getSpeed();

        double x3 = x1 + x2;
        double y3 = y1 + y2;

        double theta = Math.atan2(y3,x3);
        double thetaDegrees = (Math.toDegrees(theta) + 360) % 360;

        double speed = Math.sqrt((y1+y2)*(y1+y2) + (x1+x2)*(x1+x2));

        Velocity result = new Velocity(thetaDegrees,speed);
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
        return Math.round(otherVelocity.getDirection()*PRECISION) == Math.round(this.getDirection()*PRECISION)  && Math.round(otherVelocity.getSpeed()*PRECISION) == Math.round(this.getSpeed()*PRECISION);
    }

    @Override
    public int hashCode() {
    int result = 17;
    result = 37*result + (int) (Math.round(direction*PRECISION));
    result = 37*result + (int) (Math.round(speed*PRECISION));
    return result;
    }

    @Override
    public String toString() {
        return "Direction: " + direction + ", Speed: " + speed;
    }

    public double getDirection() {return direction;}
    public void setDirection(double direction) {this.direction = direction;}

    public double getSpeed() {return speed;}
    public void setSpeed(double speed) {this.speed = speed;}

}
