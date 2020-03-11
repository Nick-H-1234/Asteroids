/*
CLASS: Point
DESCRIPTION: Ah, if only real-life classes were this straight-forward. We'll
             use 'Point' throughout the program to store and access 
             coordinates.
*/

/**
 * Represents an immutable point on the x,y plane.
 */
class Point implements Cloneable {
    private static final double PRECISION = 1E+12;
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Point)) {
            return false;
        }
        Point otherPoint = (Point) other;
        return Math.round(otherPoint.getX()*PRECISION) == Math.round(this.getX()*PRECISION) && Math.round(otherPoint.getY()*PRECISION) == Math.round(this.getY()*PRECISION);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37*result + (int) (Math.round(x*PRECISION));
        result = 37*result + (int) (Math.round(y*PRECISION));
        return result;
    }

    public String toString() {
        return "x: " + getX() + " y: " + getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}