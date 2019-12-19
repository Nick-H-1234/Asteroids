public class Velocity {
    private double speed;
    private double direction;

    public Velocity(double direction, double speed) {
        this.direction = direction;
        this.speed = speed;
    }

    public double getSpeed() {return speed;}
    public void setSpeed() {this.speed = speed;}

    public double getDirection() {return direction;}
    public void setDirection() {this.direction = direction;}
}
