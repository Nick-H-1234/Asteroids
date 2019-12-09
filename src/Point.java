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
    private int x;
    private int y;

    public Point(int x, int y) {
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
        return otherPoint.getX() == this.getX() && otherPoint.getY() == this.getY();
    }

    @Override
    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "x: " + getX() + " y: " + getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}