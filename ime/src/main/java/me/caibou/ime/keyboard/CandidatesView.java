package me.caibou.ime.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

import me.caibou.ime.MeasureHelper;
import me.caibou.ime.OnDrawFinishListener;
import me.caibou.ime.R;

/**
 * @author caibou
 */
public class CandidatesView extends View {

    private static final String TAG = "CandidatesView";

    private static final float SELECT_BOUND_HEIGHT = MeasureHelper.CANDIDATE_VIEW_HEIGHT * 0.744680f;
    private static final float SELECT_BOUND_PADDING = MeasureHelper.KEYBOARD_WIDTH * 0.0125f;
    private static final float VIEW_WIDTH = MeasureHelper.KEYBOARD_WIDTH * 0.745833f;
    private static final float SPACING = MeasureHelper.KEYBOARD_WIDTH * 0.046875f;
    private static final float FONT_SIZE = MeasureHelper.KEYBOARD_WIDTH * 0.025f;

    private Paint paint;
    private RectF lastTextBound;
    private Scroller scroller;

    private List<String> candidates;
    private float currentBoundX, textHeight, totalLength, scrolledX;
    private int selectIndex;
    private float[] wordsX;
    private boolean cursorAlive;

    private OnDrawFinishListener drawFinishListener;

    public CandidatesView(Context context) {
        this(context, null);
    }

    public CandidatesView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CandidatesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial(context);
    }

    private void initial(Context context) {
        scroller = new Scroller(context);
        candidates = new ArrayList<>();

        paint = new Paint();
        paint.setTextSize(FONT_SIZE);
        textHeight = MeasureHelper.getFontHeight(paint);

        lastTextBound = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        currentBoundX = 0;
        int backgroundColor = getResources().getColor(R.color.keyboard_background_color);
        canvas.drawColor(backgroundColor);
        if (candidates.isEmpty()) {
            // TODO: 2018/4/23 Draw tab
        } else {
            drawCandidates(canvas);
            // Add 5 pixels as compensation for float to int
            totalLength = lastTextBound.right + 5;
        }
        if (drawFinishListener != null){
            drawFinishListener.onDrawFinish();
        }

    }

    private void drawCandidates(Canvas canvas) {
        for (int index = 0, size = candidates.size(); index < size; index++) {
            String text = candidates.get(index);
            float textWidth = paint.measureText(text);

            calTextBound(textWidth);
            if (index == selectIndex && cursorAlive) {
                drawSelectBound(canvas);
            }
            drawCandidateWord(canvas, text);
            wordsX[index] = currentBoundX;
            currentBoundX += SPACING + textWidth;
        }
    }

    private void calTextBound(float textWidth) {
        float left = currentBoundX;
        float top = (getHeight() - SELECT_BOUND_HEIGHT) / 2;
        float right = currentBoundX + textWidth + SELECT_BOUND_PADDING * 2;
        float bottom = top + SELECT_BOUND_HEIGHT;
        lastTextBound.set(left, top, right, bottom);
    }

    private void drawCandidateWord(Canvas canvas, String text) {
        paint.reset();
        paint.setTextSize(FONT_SIZE);
        paint.setColor(getResources().getColor(R.color.default_soft_key_label));
        canvas.drawText(text, currentBoundX + SELECT_BOUND_PADDING, (getHeight() + textHeight) / 2, paint);
    }

    private void drawSelectBound(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.default_key_selected_bg));
        canvas.drawRect(lastTextBound, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.default_soft_key_stroke));
        canvas.drawRect(lastTextBound, paint);
    }

    public void cursorForward() {
        int nextIndex = selectIndex + 1;
        if (nextIndex < candidates.size()) {
            int nextX = (int) wordsX[nextIndex];
            float nextWidth = paint.measureText(candidates.get(nextIndex));
            float nextCenter = nextX + nextWidth / 2 + SELECT_BOUND_PADDING;
            float scrollLimitX = totalLength - VIEW_WIDTH;
            float dstX = nextCenter - VIEW_WIDTH / 2;
            if (dstX >= scrollLimitX) {
                dstX = scrollLimitX;
            }

            if (VIEW_WIDTH / 2 <= nextCenter && scrolledX < scrollLimitX) {
                scroller.startScroll((int) scrolledX, 0, (int) (dstX - scrolledX), 0);
                scrolledX = dstX;
            }
            selectIndex = nextIndex;
            invalidate();
        }
    }

    public void cursorBackward() {
        int nextIndex = selectIndex - 1;
        if (nextIndex >= 0) {
            int nextX = (int) wordsX[nextIndex];
            float nextWidth = paint.measureText(candidates.get(nextIndex));
            float nextCenter = nextX + nextWidth / 2 + SELECT_BOUND_PADDING;
            float dstX = nextCenter - VIEW_WIDTH / 2;
            if (dstX < 0) {
                dstX = 0;
            }

            if (nextCenter < totalLength - VIEW_WIDTH / 2 && scrolledX >= 0) {
                scroller.startScroll((int) scrolledX, 0, (int) (dstX - scrolledX), 0);
                scrolledX = dstX;
            }
            selectIndex = nextIndex;
            invalidate();
        }
    }

    public boolean canForward() {
        return totalLength > VIEW_WIDTH && scrolledX < totalLength - VIEW_WIDTH;
    }

    public boolean canBackward() {
        return scrolledX > 0;
    }

    public boolean hasCandidates(){
        return !candidates.isEmpty();
    }

    public String selectCandidate() {
        return candidates.get(selectIndex);
    }

    public void clean() {
        selectIndex = 0;
        scrolledX = 0;
        totalLength = 0;
        cursorAlive = false;
        candidates.clear();
        invalidate();
    }

    public void setCursorAlive(boolean cursorAlive) {
        this.cursorAlive = cursorAlive;
        invalidate();
    }

    public boolean isCursorAlive() {
        return cursorAlive;
    }

    public void updateCandidates(List<String> candidates) {
        this.candidates.clear();
        this.candidates.addAll(candidates);
        wordsX = new float[candidates.size()];
        scrolledX = 0;
        scrollTo(0, 0);
        invalidate();
    }

    public void setDrawFinishListener(OnDrawFinishListener drawFinishListener){
        this.drawFinishListener = drawFinishListener;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) VIEW_WIDTH, heightMeasureSpec);
    }
}
