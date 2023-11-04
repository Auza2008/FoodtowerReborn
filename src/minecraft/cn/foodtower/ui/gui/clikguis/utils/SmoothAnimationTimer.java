package cn.foodtower.ui.gui.clikguis.utils;


public class SmoothAnimationTimer {

    public float target;

    public float speed = 0.3f;

    public SmoothAnimationTimer(float target) {
        this.target = target;
    }

    public SmoothAnimationTimer(float target, float speed) {
        this.target = target;
        this.speed = speed;
    }

    private float value;

    public boolean update(boolean increment) {
        value = AnimationUtil.getAnimationStateFlux(value, increment ? target : 0f, Math.max(10f, (Math.abs(value - (increment ? target : 0f))) * 40f) * speed);
        return value == target;
    }

    public void setValue(float f) {
        value = f;
    }

    public void setTarget(float scrollY) {
        target = scrollY;
    }

    public float getValue() {
        return value;
    }

    public float getTarget() {
        return target;
    }
}
