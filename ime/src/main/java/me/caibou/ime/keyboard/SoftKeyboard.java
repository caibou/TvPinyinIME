package me.caibou.ime.keyboard;

import java.util.ArrayList;
import java.util.List;

import me.caibou.ime.pattern.KeyRow;
import me.caibou.ime.pattern.SoftKey;

/**
 * @author caibou
 */
public class SoftKeyboard {

    public int backgroundColor;
    public int selectRow, selectIndex;
    public SoftKey optionsKey;
    public float verticalSpacing, horizontalSpacing;

    private List<KeyRow> rows = new ArrayList<>();

    public void addSoftKey(SoftKey softKey) {
        if (rows.isEmpty()) {
            return;
        }
        KeyRow row = rows.get(rows.size() - 1);
        row.addKey(softKey);
        if (softKey.keyCode == SkbContainer.KEYCODE_OPTIONS) {
            optionsKey = softKey;
        }
    }

    public void addKeyRow(KeyRow keyRow) {
        rows.add(keyRow);
    }

    public KeyRow getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    public int getRowNum() {
        return rows.size();
    }

    public SoftKey getSelectedKey() {
        return rows.get(selectRow).getKey(selectIndex);
    }

}
