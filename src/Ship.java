public class Ship extends Polygon {

    public static final Point[] SHIP_SHAPE = {new Point(0, 0), new Point(0, 40), new Point(60, 20)};
    private static final double MAX_SPEED = 2.5;
    private static final double DRAG = 0.01;

    boolean engineOn = false;
    private double xSpeed = 0;
    private double ySpeed = 0;
    private double acceleration = 0;

    public Ship(Point position, double direction, double scale) {
        super(SHIP_SHAPE, scale, position,-90, 0, 0, direction);
        this.scale = scale;
    }

    public void setXSpeed(double xSpeed) {this.xSpeed = xSpeed;}
    public void setYSpeed(double ySpeed) {this.ySpeed = ySpeed;}
    public void setAcceleration(double acceleration) {this.acceleration = acceleration;}

    public void reset(Point position) {
        setAcceleration(0);
        setSpeed(0);
        setXSpeed(0);
        setYSpeed(0);
        this.position = position;
    }

    public void setRotation(double rotation) {this.rotation = rotation;
    }

    public boolean move(int width, int height) {

//        if (!engineOn) {
//            acceleration = 0;
//        }
//        else if (engineOn) {
//            acceleration = 0.25;
//        }
//
//        double effectiveAcceleration = acceleration;
//        if (!engineOn && speed < 0.05) {
//            effectiveAcceleration = 0;
//        }
//        else if (!engineOn) {
//            effectiveAcceleration = -drag;
//        }
//
//        speed += effectiveAcceleration;
//        speed = Math.min(speed, maxSpeed);
//        if (speed < 0.05) {
//            speed = 0;
//        }
//        xSpeed = speed * Math.cos(Math.toRadians(rotation));
//        ySpeed = speed * Math.sin(Math.toRadians(rotation));
//


        if (engineOn) {
            acceleration = 0.1;
        }
        else if (!engineOn) {
            acceleration = 0;
        }

        xSpeed += acceleration * Math.cos(Math.toRadians(rotation));
        ySpeed += acceleration * Math.sin(Math.toRadians(rotation));

        if (xSpeed > 0.05) {
            if (xSpeed > MAX_SPEED) {
                xSpeed = MAX_SPEED;
            }
            else if (!engineOn) {
                xSpeed -= DRAG;
            }
        }
        else if (xSpeed < -0.05) {
            if (xSpeed < 0- MAX_SPEED) {
                xSpeed = 0- MAX_SPEED;
            }
            else if (!engineOn) {
                xSpeed += DRAG;
            }
        }
        if (ySpeed > 0.05) {
            if (ySpeed > MAX_SPEED) {
                ySpeed = MAX_SPEED;
            }
            else if (!engineOn){
                ySpeed -= DRAG;
            }
        }
        else if (ySpeed < -0.05) {
            if (ySpeed < 0- MAX_SPEED) {
                ySpeed = 0- MAX_SPEED;
            }
            else if (!engineOn) {
                ySpeed += DRAG;
            }
        }
        double xPos = position.getX();
        double yPos = position.getY();
        xPos += xSpeed;
        yPos += ySpeed;

        if (xPos > width) {
            xPos = 0;
        } else if (xPos < 0) {
            xPos = width;
        }
        if (yPos > height) {
            yPos = 0;
        } else if (yPos < 0) {
            yPos = height;
        }

        Point newPosition = new Point(xPos, yPos);
        setPosition(newPosition);
        return true;
    }

}