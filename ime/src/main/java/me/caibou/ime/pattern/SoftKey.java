package me.caibou.ime.pattern;

import android.graphics.drawable.Drawable;

/**
 * @author caibou
 */
public class SoftKey extends Element {

    private static final int CUSTOMIZE_KEYCODE_START = -200;
    private static final int CUSTOMIZE_KEYCODE_END = -100;

    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;

    public int keyCode;
    public String keyLabel;
    public int textColor, normalColor, selectedColor, pressedColor, strokeColor;
    public float textSize, iconWidth, iconHeight, strokeWidth;

    public int crossRow, crossColumn;
    public int orientation = HORIZONTAL;

    public Drawable icon;

    public boolean selected;
    public boolean pressed;

    public boolean isCustomizeKey() {
        return CUSTOMIZE_KEYCODE_START <= keyCode && keyCode <= CUSTOMIZE_KEYCODE_END;
    }

    @Override
    public String toString() {
        return "SoftKey{" +
                "keyCode=" + keyCode +
                ", keyLabel='" + keyLabel + '\'' +
                ", selected=" + selected +
                ", pressed=" + pressed +
                ", left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
