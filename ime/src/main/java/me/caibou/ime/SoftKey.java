package me.caibou.ime;

/**
 * @author caibou
 */
public class SoftKey {

    private static final int CUSTOMIZE_KEYCODE_START = -200;
    private static final int CUSTOMIZE_KEYCODE_END = -100;

    private int keyCode;
    private String keyLabel;
    private int textColor;
    private float textSize;

    private boolean selected;
    private boolean pressed;

    public boolean isCustomizeKey() {
        return CUSTOMIZE_KEYCODE_START < keyCode && keyCode < CUSTOMIZE_KEYCODE_END;
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
