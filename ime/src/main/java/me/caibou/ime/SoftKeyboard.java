package me.caibou.ime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caibou
 */
public class SoftKeyboard {

    private int backgroundColor;
    private int keyTextColor, keyNormalColor, keyStrokeColor, keySelectedColor;
    private int keyWidth, keyHeight, keyStrokeWidth, keyIconSize;
    private int keysSpacing;


    private List<KeyRow> rows = new ArrayList<>();

    public void addSoftKey(SoftKey softKey){
        if (rows.isEmpty()){
            return;
        }
        KeyRow row = rows.get(rows.size() - 1);
        row.addKey(softKey);
    }

    public void addKeyRow(KeyRow keyRow){
        rows.add(keyRow);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getKeyTextColor() {
        return keyTextColor;
    }

    public void setKeyTextColor(int keyTextColor) {
        this.keyTextColor = keyTextColor;
    }

    public int getKeyNormalColor() {
        return keyNormalColor;
    }

    public void setKeyNormalColor(int keyNormalColor) {
        this.keyNormalColor = keyNormalColor;
    }

    public int getKeyStrokeColor() {
        return keyStrokeColor;
    }

    public void setKeyStrokeColor(int keyStrokeColor) {
        this.keyStrokeColor = keyStrokeColor;
    }

    public int getKeySelectedColor() {
        return keySelectedColor;
    }

    public void setKeySelectedColor(int keySelectedColor) {
        this.keySelectedColor = keySelectedColor;
    }

    public int getKeyWidth() {
        return keyWidth;
    }

    public void setKeyWidth(int keyWidth) {
        this.keyWidth = keyWidth;
    }

    public int getKeyHeight() {
        return keyHeight;
    }

    public void setKeyHeight(int keyHeight) {
        this.keyHeight = keyHeight;
    }

    public int getKeyStrokeWidth() {
        return keyStrokeWidth;
    }

    public void setKeyStrokeWidth(int keyStrokeWidth) {
        this.keyStrokeWidth = keyStrokeWidth;
    }

    public int getKeyIconSize() {
        return keyIconSize;
    }

    public void setKeyIconSize(int keyIconSize) {
        this.keyIconSize = keyIconSize;
    }

    public int getKeysSpacing() {
        return keysSpacing;
    }

    public void setKeysSpacing(int keysSpacing) {
        this.keysSpacing = keysSpacing;
    }

    public List<KeyRow> getRows() {
        return rows;
    }

    public void setRows(List<KeyRow> rows) {
        this.rows = rows;
    }
}
