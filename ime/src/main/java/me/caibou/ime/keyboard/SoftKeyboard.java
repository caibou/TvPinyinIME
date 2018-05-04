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

    public void setImeOptions(Resources res, int options) {
        if (optionsKey == null){
            return;
        }
        switch (options&(EditorInfo.IME_MASK_ACTION|EditorInfo.IME_FLAG_NO_ENTER_ACTION)) {
            case EditorInfo.IME_ACTION_GO:
                optionsKey.keyLabel = res.getString(R.string.label_go_key);
                break;
            case EditorInfo.IME_ACTION_NEXT:
                optionsKey.keyLabel = res.getString(R.string.label_next);
                break;
            case EditorInfo.IME_ACTION_SEND:
                optionsKey.keyLabel = res.getString(R.string.label_send);
                break;
            case EditorInfo.IME_ACTION_DONE:
                optionsKey.keyLabel = res.getString(R.string.label_finish);
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

    public SoftKey getSelectedKey() {
        return rows.get(selectRow).getKey(selectIndex);
    }

}
