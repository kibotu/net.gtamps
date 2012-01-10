package net.gtamps.android.core.input.touch;

public class TouchInputButton {
    private float x;
    private float y;
    private float sizeX;
    private float sizeY;

    public TouchInputButton(float x, float y, float sizeX, float sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public boolean isHit(float px, float py) {
        return (px >= x && py >= y && px <= x + sizeX && py <= y + sizeY);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSizeX() {
        return sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }
}
