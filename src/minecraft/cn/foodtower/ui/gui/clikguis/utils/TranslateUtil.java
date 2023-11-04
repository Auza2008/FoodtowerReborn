package cn.foodtower.ui.gui.clikguis.utils;

public class TranslateUtil {
    private float x;
    private float y;
    private long lastMS;

    public TranslateUtil(float x, float y) {
        this.x = x;
        this.y = y;
        lastMS = System.currentTimeMillis();
    }

    public void interpolate(float targetX, float targetY, float smoothing) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - lastMS;
        lastMS = currentMS;
        int deltaX = (int) (Math.abs(targetX - x) * smoothing);
        int deltaY = (int) (Math.abs(targetY - y) * smoothing);
        x = AnimationUtil.calculateCompensation(targetX, x, delta, deltaX);
        y = AnimationUtil.calculateCompensation(targetY, y, delta, deltaY);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getValue() {
        return y;
    }
}
