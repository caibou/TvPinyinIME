package me.caibou.ime.keyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import me.caibou.ime.R;
import me.caibou.ime.pattern.SoftKey;

/**
 * @author caibou
 */
public class SkbContainer extends FrameLayout {

    private SoftKeyboardView keyboardView;
    private SoftKeyboard softKeyboard;
    private KeyboardListener listener;

    private static final int KEYCODE_SWITCH_TO_QWERTY_CN = -100;
    private static final int KEYCODE_SWITCH_TO_EN = -101;
    private static final int KEYCODE_SWITCH_TO_NUMBER = -103;
    private static final int KEYCODE_COMMIT_LABEL = -105;
    private static final int KEYCODE_COMPOSE_WORDS = -106;

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
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                softKeyboard.setSelectIndex(softKeyboard.getSelectIndex() - 1);
                keyboardView.invalidate();
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                softKeyboard.setSelectIndex(softKeyboard.getSelectIndex() + 1);
                keyboardView.invalidate();
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                softKeyboard.setSelectRow(softKeyboard.getSelectRow() - 1);
                keyboardView.invalidate();
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                softKeyboard.setSelectRow(softKeyboard.getSelectRow() + 1);
                keyboardView.invalidate();
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                softKeyboard.getRow(softKeyboard.getSelectRow()).getKey(softKeyboard.getSelectIndex()).setPressed(true);
                keyboardView.invalidate();
                return true;

        }
        return false;
    }

    public boolean onSoftKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                SoftKey softKey = softKeyboard.getRow(softKeyboard.getSelectRow()).getKey(softKeyboard.getSelectIndex());
                if (softKey != null){
                    softKey.setPressed(false);
                    if (softKey.isCustomizeKey()){
                        processCustomizeKey(softKey);
                    } else {
                        listener.onSoftKeyClick(softKey);
                    }

                }
                return true;
        }
        return false;
    }

    private void processCustomizeKey(SoftKey softKey) {
        switch (softKey.getKeyCode()){
            case KEYCODE_COMMIT_LABEL:
                listener.onCommitText(softKey.getKeyLabel());
                break;
            case KEYCODE_SWITCH_TO_EN:
                updateKeyboardLayout(R.xml.skb_qwerty_en);
                break;
            case KEYCODE_SWITCH_TO_QWERTY_CN:
                updateKeyboardLayout(R.xml.skb_qwerty_cn);
                break;
            case KEYCODE_SWITCH_TO_NUMBER:
                updateKeyboardLayout(R.xml.skb_number);
                break;
        }
    }

    public void setKeyboardListener(KeyboardListener listener) {
        this.listener = listener;
    }

    private void updateKeyboardLayout(int layoutId){
        softKeyboard = new KeyboardLoader(getContext()).load(layoutId);
        keyboardView.setSoftKeyboard(softKeyboard);
        keyboardView.invalidate();
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
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(340, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
