package me.caibou.ime.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import me.caibou.ime.MeasureHelper;
import me.caibou.ime.OnDrawFinishListener;
import me.caibou.ime.R;

/**
 * @author caibou
 */
public class CandidateContainer extends RelativeLayout implements OnDrawFinishListener {

    private static final float VIEW_HEIGHT = MeasureHelper.KEYBOARD_WIDTH * 0.048958f;
    public CandidatesView candidatesView;
    private ImageView ivLeftMore, ivRightMore;
    private KeyboardListener keyboardListener;

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
                if (candidatesView.isCursorAlive()) {
                    candidatesView.setCursorAlive(false);
                }
                break;
        }
        return false;
    }

    public boolean onSoftKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (candidatesView.isCursorAlive()) {
                    String word = candidatesView.selectCandidate();
                    candidatesView.clean();
                    candidatesView.setCursorAlive(false);
                    keyboardListener.onCommitText(word);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                if (candidatesView.hasCandidates()){
                    candidatesView.clean();
                    candidatesView.setCursorAlive(false);
                    keyboardListener.onCommitText("");
                    return true;
                }
                break;
        }
        return false;
    }

    public void setKeyboardListener(KeyboardListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    public void updateCandidates(List<String> candidates) {
        candidatesView.updateCandidates(candidates);
    }

    private void updateMoreIconStatus() {
        ivLeftMore.setVisibility(candidatesView.canBackward() ? VISIBLE : GONE);
        ivRightMore.setVisibility(candidatesView.canForward() ? VISIBLE : GONE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        candidatesView = findViewById(R.id.candi_view);
        candidatesView.setDrawFinishListener(this);
        ivLeftMore = findViewById(R.id.iv_left_more);
        ivRightMore = findViewById(R.id.iv_right_more);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureHelper.KEYBOARD_WIDTH, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) VIEW_HEIGHT, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDrawFinish() {
        updateMoreIconStatus();
    }
}
