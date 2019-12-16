public class Effect extends Circle {
    private int duration;

    /**
     * Creates a circle centered on position, with radius radius.
     * @param radius   the radius of the circle.
     * @param position the centre of the circle.
     * @param direction the rotation of the circle.
     * @param duration the duration of the effect in frames.
     */
    public Effect(int radius, Point position, double direction, int duration) {
        super(radius, position, direction, 0);
        this.duration = duration;
    }

    @Override
    public boolean move(int width, int height) {
        super.move(width, height);
        duration--;
        return duration!=0;
    }
}
