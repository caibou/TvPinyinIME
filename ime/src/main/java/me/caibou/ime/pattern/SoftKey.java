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

    private int keyCode;
    private String keyLabel;
    private int textColor, normalColor, selectedColor, pressedColor, strokeColor;
    private float textSize, iconWidth, iconHeight, strokeWidth;

    private int crossRow, crossColumn;
    private int orientation = HORIZONTAL;


    private Drawable icon;

    private boolean selected;
    private boolean pressed;

    public boolean isCustomizeKey() {
        return CUSTOMIZE_KEYCODE_START <= keyCode && keyCode <= CUSTOMIZE_KEYCODE_END;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        this.keyLabel = keyLabel;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getCrossRow() {
        return crossRow;
    }

    public void setCrossRow(int crossRow) {
        this.crossRow = crossRow;
    }

    public int getCrossColumn() {
        return crossColumn;
    }

    public void setCrossColumn(int crossColumn) {
        this.crossColumn = crossColumn;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public int getPressedColor() {
        return pressedColor;
    }

    public void setPressedColor(int pressedColor) {
        this.pressedColor = pressedColor;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public float getIconWidth() {
        return iconWidth;
    }

    public void setIconWidth(float iconWidth) {
        this.iconWidth = iconWidth;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public float getIconHeight() {
        return iconHeight;
    }

    public void setIconHeight(float iconHeight) {
        this.iconHeight = iconHeight;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "SoftKey{" +
                "keyCode=" + keyCode +
                ", selected=" + selected +
                ", pressed=" + pressed +
                ", keyLabel='" + keyLabel + '\'' +
                '}';
    }
}
