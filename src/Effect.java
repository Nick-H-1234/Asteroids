public class Effect extends Circle {

    private int duration;

    /**
     * Creates a circle centered on position, with radius radius.
     *  @param position the centre of the circle.
     * @param radius   the radius of the circle.
     * @param rotation the rotation of the circle.
     * @param duration the duration of the effect in frames.
     */
    public Effect(Point position, int radius, double rotation, int duration) {
        super(position, radius, rotation);
        this.duration = duration;
    }

    @Override
    public boolean move(int width, int height) {
        super.move(width, height);
        duration--;
        return duration!=0;
    }
}
