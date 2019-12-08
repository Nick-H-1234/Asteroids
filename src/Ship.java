public class Ship extends Polygon {

    public static final Point[] SHIP_SHAPE = {new Point(0, 0), new Point(0, 40), new Point(60, 20)};

    public Ship(Point position, double rotation) {
        super(SHIP_SHAPE, position, rotation);
        xPos = position.x;
        yPos = position.y;
    }

    boolean engineOn = false;

    private double xSpeed = 0;
    private double ySpeed = 0;
    private double acceleration = 0;
    private double maxSpeed = 3.5;
    private double drag = 0.03;

    private double xPos, yPos;



    public void setXSpeed(double xSpeed) {this.xSpeed = xSpeed;}
    public void setYSpeed(double ySpeed) {this.ySpeed = ySpeed;}
    public void setAcceleration(double acceleration) {this.acceleration = acceleration;}

    public void setXPos(double xPos) {this.xPos = xPos;}
    public void setYPos(double yPos) {this.yPos = yPos;}

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
            acceleration = 0.2;
        }
        else if (!engineOn) {
            acceleration = 0;
        }

        xSpeed += acceleration * Math.cos(Math.toRadians(rotation));
        ySpeed += acceleration * Math.sin(Math.toRadians(rotation));

        if (xSpeed > 0.05) {
            if (xSpeed > maxSpeed) {
                xSpeed = maxSpeed;
            }
            else if (!engineOn) {
                xSpeed -= drag;
            }
        }
        else if (xSpeed < -0.05) {
            if (xSpeed < 0-maxSpeed) {
                xSpeed = 0-maxSpeed;
            }
            else if (!engineOn) {
                xSpeed += drag;
            }
        }
        if (ySpeed > 0.05) {
            if (ySpeed > maxSpeed) {
                ySpeed = maxSpeed;
            }
            else if (!engineOn){
                ySpeed -= drag;
            }
        }
        else if (ySpeed < -0.05) {
            if (ySpeed < 0-maxSpeed) {
                ySpeed = 0-maxSpeed;
            }
            else if (!engineOn) {
                ySpeed += drag;
            }
        }
        xPos += xSpeed;
        yPos += ySpeed;
        position.x = (int) xPos;
        position.y = (int) yPos;

        Point centre = findCenter();
        if (position.x > width) {
            xPos = 0;
            position.x = 0;
        } else if (position.x < 0) {
            xPos = width;
            position.x = width;
        }
        if (position.y > height) {
            yPos = 0;
            position.y = 0;
        } else if (position.y < 0) {
            yPos = height;
            position.y = height;
        }


        return true;
    }

}