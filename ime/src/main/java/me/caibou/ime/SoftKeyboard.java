package me.caibou.ime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caibou
 */
public class SoftKeyboard {

    private int backgroundColor;
    private float keysSpacing;
    private boolean isUpperCase;

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

    public KeyRow getRow(int rowIndex){
        return rows.get(rowIndex);
    }

    public int getRowNum(){
        return rows.size();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getKeysSpacing() {
        return keysSpacing;
    }

    public void setKeysSpacing(float keysSpacing) {
        this.keysSpacing = keysSpacing;
    }

}