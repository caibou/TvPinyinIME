package me.caibou.ime;

import java.util.ArrayList;
import java.util.List;

import me.caibou.ime.pattern.SoftKey;

/**
 * @author caibou
 */
public class SoftKeyboard {

    private int backgroundColor;
    private float keysSpacing;
    private boolean isUpperCase;

    private int selectRow, selectIndex;
    private SoftKey selectedKey;

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

    public int getSelectRow() {
        return selectRow;
    }

    public void setSelectRow(int selectRow) {
        this.selectRow = selectRow;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public SoftKey getSelectedKey() {
        return selectedKey;
    }

    public void setSelectedKey(SoftKey selectedKey) {
        this.selectedKey = selectedKey;
    }
}
