package me.caibou.ime.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.widget.RelativeLayout;

import me.caibou.ime.MeasureHelper;
import me.caibou.ime.R;

/**
 * @author caibou
 */
public class CandidateContainer extends RelativeLayout {

    private static final float VIEW_HEIGHT = MeasureHelper.SCREEN_HEIGHT * 0.087037f;
    public CandidatesView candidatesView;
    public InputMethodService inputMethodService;

    public CandidateContainer(Context context) {
        super(context);
    }

    public CandidateContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CandidateContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean onSoftKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (candidatesView.isCursorAlive()) {
                    candidatesView.cursorBackward();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (candidatesView.isCursorAlive()) {
                    candidatesView.cursorForward();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (candidatesView.isCursorAlive()) {
                    return true;
                }
                break;
            case KeyEvent.ACTION_DOWN:
                if (candidatesView.isCursorAlive()){
                    candidatesView.cancelSelected();
                }
                break;
        }
        return false;
    }

    public boolean onSoftKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (candidatesView.isCursorAlive()){
                    String word = candidatesView.selectCandidate();
                    candidatesView.clean();
                    candidatesView.setCursorAlive(false);
                    InputConnection ic = inputMethodService.getCurrentInputConnection();
                    ic.commitText(word, 0);
                    return true;
                }
                break;
        }
        return false;
    }

    public void setInputMethodService(InputMethodService inputMethodService){
        this.inputMethodService = inputMethodService;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        candidatesView = findViewById(R.id.candi_view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureHelper.SCREEN_WIDTH, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) VIEW_HEIGHT, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
