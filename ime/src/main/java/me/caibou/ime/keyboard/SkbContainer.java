package me.caibou.ime.keyboard;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import me.caibou.ime.InputMethodSwitcher;
import me.caibou.ime.MeasureHelper;
import me.caibou.ime.R;
import me.caibou.ime.pattern.SoftKey;

/**
 * @author caibou
 */
public class SkbContainer extends FrameLayout {

    private SoftKeyboardView keyboardView;
    private CandidatesView candidatesView;
    private SoftKeyboard softKeyboard;
    private KeyboardListener listener;

    private static final float VIEW_HEIGHT = MeasureHelper.SCREEN_HEIGHT * 0.314814f;

    public static final int KEYCODE_SWITCH_TO_QWERTY_CN = -100;
    public static final int KEYCODE_SWITCH_TO_EN = -101;
    public static final int KEYCODE_SWITCH_TO_NUMBER = -103;
    public static final int KEYCODE_CANDI_SYMBOL = -104;
    public static final int KEYCODE_COMMIT_LABEL = -105;
    public static final int KEYCODE_COMPOSE_WORDS = -106;
    public static final int KEYCODE_CASE_SWITCH = -107;
    public static final int KEYCODE_OPTIONS = -108;

    public SkbContainer(@NonNull Context context) {
        this(context, null);
    }

    public SkbContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkbContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean onSoftKeyDown(int keyCode, KeyEvent event) {
        int selectRow = softKeyboard.selectRow;
        int selectIndex = softKeyboard.selectIndex;
        int rowSize = softKeyboard.getRow(selectRow).keyCount();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (selectIndex - 1 >= 0) {
                    selectIndex--;
                    softKeyboard.selectIndex = selectIndex;
                    keyboardView.invalidate();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (selectIndex + 1 < rowSize) {
                    selectIndex++;
                    softKeyboard.selectIndex = selectIndex;
                    keyboardView.invalidate();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (selectRow - 1 >= 0) {
                    selectRow--;
                    softKeyboard.selectRow = selectRow;
                } else if (candidatesView.hasCandidates()){
                    softKeyboard.getSelectedKey().pressed = false;
                    keyboardView.setCursorAlive(false);
                    candidatesView.setCursorAlive(true);
                }
                keyboardView.invalidate();
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (!keyboardView.isCursorAlive()){
                    keyboardView.setCursorAlive(true);
                    candidatesView.setCursorAlive(false);
                } else if (selectRow + 1 < softKeyboard.getRowNum()) {
                    selectRow++;
                    softKeyboard.selectRow = selectRow;
                    keyboardView.invalidate();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                softKeyboard.getSelectedKey().pressed = true;
                keyboardView.invalidate();
                return true;

        }
        return false;
    }

    public boolean onSoftKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                SoftKey softKey = softKeyboard.getSelectedKey();
                if (softKey != null) {
                    softKey.pressed = false;
                    softKey.selected = false;
                    if (softKey.isCustomizeKey()) {
                        processCustomizeKey(softKey);
                    } else {
                        listener.onSoftKeyClick(softKey);
                    }

                }
                keyboardView.invalidate();
                return true;
        }
        return false;
    }

    private void processCustomizeKey(SoftKey softKey) {
        switch (softKey.keyCode) {
            case KEYCODE_COMMIT_LABEL:
                listener.onCommitText(softKey.keyLabel);
                break;
            case KEYCODE_SWITCH_TO_EN:
                updateKeyboardLayout(R.xml.skb_qwerty_en);
                break;
            case KEYCODE_SWITCH_TO_QWERTY_CN:
                InputMethodSwitcher.getInstance().setUpperCase(false);
                updateKeyboardLayout(R.xml.skb_qwerty_cn);
                break;
            case KEYCODE_SWITCH_TO_NUMBER:
                updateKeyboardLayout(R.xml.skb_number);
                break;
            case KEYCODE_CASE_SWITCH:
                InputMethodSwitcher switcher = InputMethodSwitcher.getInstance();
                InputMethodSwitcher.getInstance().setUpperCase(!switcher.isUpperCase());
                keyboardView.invalidate();
                break;
            case KEYCODE_COMPOSE_WORDS:
            case KEYCODE_CANDI_SYMBOL:
            case KEYCODE_OPTIONS:
                listener.onSoftKeyClick(softKey);
                break;
        }
    }

    public void setKeyboardListener(KeyboardListener listener) {
        this.listener = listener;
    }

    public void setCandidatesView(CandidatesView candidatesView) {
        this.candidatesView = candidatesView;
    }

    public void setImeOptions(Resources res, int options) {
        softKeyboard.setImeOptions(res, options);
        keyboardView.invalidate();
    }

    public void keyboardFocus(){
        keyboardView.setCursorAlive(true);
    }

    private void updateKeyboardLayout(int layoutId) {
        softKeyboard = new KeyboardLoader(getContext()).load(layoutId);
        keyboardView.setSoftKeyboard(softKeyboard);
        keyboardView.invalidate();
        InputMethodSwitcher.getInstance().setKeyboardLayoutId(layoutId);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        keyboardView = findViewById(R.id.keyboard_view);
        updateKeyboardLayout(R.xml.skb_qwerty_en);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) VIEW_HEIGHT, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
