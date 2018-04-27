package me.caibou.ime.keyboard;

import android.content.res.Resources;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;
import java.util.List;

import me.caibou.ime.R;
import me.caibou.ime.pattern.KeyRow;
import me.caibou.ime.pattern.SoftKey;

/**
 * @author caibou
 */
public class SoftKeyboard {

    private int backgroundColor;
    private float keysSpacing;

    private int selectRow, selectIndex;
    private SoftKey selectedKey, optionsKey;

    private List<KeyRow> rows = new ArrayList<>();

    public void addSoftKey(SoftKey softKey) {
        if (rows.isEmpty()) {
            return;
        }
        KeyRow row = rows.get(rows.size() - 1);
        row.addKey(softKey);
        if (softKey.getKeyCode() == SkbContainer.KEYCODE_OPTIONS) {
            optionsKey = softKey;
        }
    }

    public void setImeOptions(Resources res, int options) {
        if (optionsKey == null){
            return;
        }
        switch (options&(EditorInfo.IME_MASK_ACTION|EditorInfo.IME_FLAG_NO_ENTER_ACTION)) {
            case EditorInfo.IME_ACTION_GO:
                optionsKey.setKeyLabel(res.getString(R.string.label_go_key));
                break;
            case EditorInfo.IME_ACTION_NEXT:
                optionsKey.setKeyLabel(res.getString(R.string.label_next));
                break;
            case EditorInfo.IME_ACTION_SEND:
                optionsKey.setKeyLabel(res.getString(R.string.label_send));
                break;
            case EditorInfo.IME_ACTION_DONE:
                optionsKey.setKeyLabel(res.getString(R.string.label_finish));
                break;
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
        return rows.get(selectRow).getKey(selectIndex);
    }

    public void setSelectedKey(SoftKey selectedKey) {
        this.selectedKey = selectedKey;
    }

    public void cleanSelect() {
        selectedKey = null;
    }

    public void reSelect() {
        this.selectedKey = rows.get(selectRow).getKey(selectIndex);
    }

    public boolean isSelected() {
        return selectedKey != null;
    }

}
