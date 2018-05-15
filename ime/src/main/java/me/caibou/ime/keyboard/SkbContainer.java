package me.caibou.ime.keyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import me.caibou.ime.InputMethodSwitcher;
import me.caibou.ime.MeasureHelper;
import me.caibou.ime.R;
import me.caibou.ime.pattern.KeyRow;
import me.caibou.ime.pattern.SoftKey;

/**
 * @author caibou
 */
public class SkbContainer extends FrameLayout {

    private static final String TAG = "SkbContainer";

    private SoftKeyboardView keyboardView;
    private CandidatesView candidatesView;
    private SoftKeyboard softKeyboard;
    private KeyboardListener listener;

    public static final int KEYCODE_SWITCH_TO_QWERTY_CN = -100;
    public static final int KEYCODE_SWITCH_TO_EN = -101;
    public static final int KEYCODE_SWITCH_TO_NUMBER = -103;
    public static final int KEYCODE_CANDI_SYMBOL = -104;
    public static final int KEYCODE_COMMIT_LABEL = -105;
    public static final int KEYCODE_COMPOSE_WORDS = -106;
    public static final int KEYCODE_CASE_SWITCH = -107;
    public static final int KEYCODE_OPTIONS = -108;

    public static final int FIX_LENGTH = 10;

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
        SoftKey currKey = softKeyboard.getSelectedKey();
        float nextX, nextY;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                nextX = currKey.left - softKeyboard.horizontalSpacing - FIX_LENGTH;
                nextY = currKey.bottom - FIX_LENGTH;
                mapKey(nextX, nextY);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                nextX = currKey.right + softKeyboard.horizontalSpacing + FIX_LENGTH;
                nextY = currKey.bottom - FIX_LENGTH;
                mapKey(nextX, nextY);
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                nextX = currKey.left + FIX_LENGTH;
                nextY = currKey.top - softKeyboard.verticalSpacing - FIX_LENGTH;
                if (!mapKey(nextX, nextY) && candidatesView.hasCandidates()) {
                    currKey.pressed = false;
                    keyboardView.setCursorAlive(false);
                    candidatesView.setCursorAlive(true);
                    keyboardView.invalidate();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                currKey.pressed = false;
                if (keyboardView.isCursorAlive()){
                    nextX = currKey.left + FIX_LENGTH;
                    nextY = currKey.bottom + softKeyboard.verticalSpacing + FIX_LENGTH;
                    mapKey(nextX, nextY);
                } else {
                    keyboardView.setCursorAlive(true);
                    candidatesView.setCursorAlive(false);
                    keyboardView.invalidate();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                currKey.pressed = true;
                keyboardView.invalidate();
                return true;

        }
        return false;
    }

    private boolean          mapKey(float nextX, float nextY) {
        for (int i = 0, size = softKeyboard.getRowNum(); i < size; i++) {
            KeyRow keyRow = softKeyboard.getRow(i);
            for (int index = 0, num = keyRow.keyCount(); index < num; index++) {
                SoftKey softKey = keyRow.getKey(index);
                if (softKey.left <= nextX && nextX <= softKey.right &&
                        softKey.top <= nextY && nextY <= softKey.bottom) {
                    softKeyboard.selectRow = i;
                    softKeyboard.selectIndex = index;
                    keyboardView.invalidate();
                    return true;
                }
            }
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

    public void keyboardFocus() {
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
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureHelper.KEYBOARD_HEIGHT, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
