public class Ship extends Polygon {

    public static final Point[] SHIP_SHAPE = {new Point(0, 0), new Point(0, 40), new Point(60, 20)};
    private static final double MAX_SPEED = 2.8;
    private static final double DRAG = 0.006;
    private static final double ACCELERATION = 0.08;

    boolean engineOn = false;
    private double acceleration = 0;

    public Ship(Point position, double scale, Velocity velocity) {
        super(SHIP_SHAPE, scale, position,-90, 0, velocity);
    }

//    public void setXSpeed(double xSpeed) {this.xSpeed = xSpeed;}
//    public void setYSpeed(double ySpeed) {this.ySpeed = ySpeed;}
    public void setAcceleration(double acceleration) {this.acceleration = acceleration;}

    public void reset(Point position) {
        setAcceleration(0);
        velocity.setSpeed(0);
        setRotation(-90);
//        setXSpeed(0);
//        setYSpeed(0);
        this.position = position;
    }

    public void setRotation(double rotation) {this.rotation = rotation;
    }

    public boolean move(int width, int height) {

//        if (!engineOn && velocity.getSpeed() > 0.05)  {
//            Velocity drag = new Velocity(velocity.getDirection() - 180, DRAG);
//            velocity = Velocity.add(velocity, drag);
//        }

//        if (!engineOn && velocity.getSpeed() < 0.05) {
//            velocity.setSpeed(0);
//        }

        if (engineOn) {
            Velocity currentVelocity = velocity;
            Velocity newVelocity = new Velocity(getRotation(), ACCELERATION);
            velocity = Velocity.add(currentVelocity, newVelocity);
        }

        velocity.setSpeed(Math.min(velocity.getSpeed(), MAX_SPEED));

        double x = position.getX() + (velocity.getSpeed() * Math.cos(Math.toRadians(velocity.getDirection())));
        double y = position.getY() + (velocity.getSpeed() * Math.sin(Math.toRadians(velocity.getDirection())));

        if (x < 0) {x = width;}
        if (x > width) {x = 0;}

        if (y < 0) {y = height;}
        if (y > height) {y = 0;}

        Point newPosition = new Point(x,y);
        setPosition(newPosition);

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


//        if (engineOn) {
//            acceleration = 0.1;
//        }
//        else if (!engineOn) {
//            acceleration = 0;
//        }
//
//        xSpeed += acceleration * Math.cos(Math.toRadians(rotation));
//        ySpeed += acceleration * Math.sin(Math.toRadians(rotation));
//
//        if (xSpeed > 0.05) {
//            if (xSpeed > MAX_SPEED) {
//                xSpeed = MAX_SPEED;
//            }
//            else if (!engineOn) {
//                xSpeed -= DRAG;
//            }
//        }
//        else if (xSpeed < -0.05) {
//            if (xSpeed < 0- MAX_SPEED) {
//                xSpeed = 0- MAX_SPEED;
//            }
//            else if (!engineOn) {
//                xSpeed += DRAG;
//            }
//        }
//        if (ySpeed > 0.05) {
//            if (ySpeed > MAX_SPEED) {
//                ySpeed = MAX_SPEED;
//            }
//            else if (!engineOn){
//                ySpeed -= DRAG;
//            }
//        }
//        else if (ySpeed < -0.05) {
//            if (ySpeed < 0- MAX_SPEED) {
//                ySpeed = 0- MAX_SPEED;
//            }
//            else if (!engineOn) {
//                ySpeed += DRAG;
//            }
//        }
//        double xPos = position.getX();
//        double yPos = position.getY();
//        xPos += xSpeed;
//        yPos += ySpeed;
//
//        if (xPos > width) {
//            xPos = 0;
//        } else if (xPos < 0) {
//            xPos = width;
//        }
//        if (yPos > height) {
//            yPos = 0;
//        } else if (yPos < 0) {
//            yPos = height;
//        }
//
//        Point newPosition = new Point(xPos, yPos);
//        setPosition(newPosition);



        return true;
    }

}