package me.caibou.ime.pattern;

/**
 * @author caibou
 */
public class Element {

    private float left, top, right, bottom;
    private float width, height;

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        fixRange();
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        fixRange();
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
        fixRange();
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
        fixRange();
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public void fixRange() {
        right = left + width;
        bottom = top + height;
    }

}
