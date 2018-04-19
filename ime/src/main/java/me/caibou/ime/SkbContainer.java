package me.caibou.ime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;

/**
 * @author caibou
 */
public class SkbContainer extends FrameLayout {

    private SoftKeyboardView keyboardView;
    private SoftKeyboard softKeyboard;

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
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_LEFT:
                softKeyboard.setSelectIndex(softKeyboard.getSelectIndex() - 1);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                softKeyboard.setSelectIndex(softKeyboard.getSelectIndex() + 1);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                softKeyboard.setSelectRow(softKeyboard.getSelectRow() - 1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                softKeyboard.setSelectRow(softKeyboard.getSelectRow() + 1);
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                softKeyboard.getRow(softKeyboard.getSelectRow()).getKey(softKeyboard.getSelectIndex()).setPressed(true);
                break;

        }
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                keyboardView.invalidate();
                return true;
        }
        return false;
    }

    public boolean onSoftKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                softKeyboard.getRow(softKeyboard.getSelectRow()).getKey(softKeyboard.getSelectIndex()).setPressed(false);
                break;

        }
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                keyboardView.invalidate();
                return true;
        }
        return false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        keyboardView = findViewById(R.id.keyboard_view);
        softKeyboard = new KeyboardLoader(getContext()).load(R.xml.skb_number);
        keyboardView.setSoftKeyboard(softKeyboard);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(436, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
