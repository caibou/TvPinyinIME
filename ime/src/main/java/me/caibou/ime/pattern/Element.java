package me.caibou.ime.pattern;

/**
 * @author caibou
 */
public class Element {

    public float left, top, right, bottom;
    public float width, height;

    public void fixRange() {
        right = left + width;
        bottom = top + height;
    }

}
