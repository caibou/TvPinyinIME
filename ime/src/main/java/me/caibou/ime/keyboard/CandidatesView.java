package me.caibou.ime.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

import me.caibou.ime.MeasureHelper;
import me.caibou.ime.R;

/**
 * @author caibou
 */
public class CandidatesView extends View {

    private static final float SELECT_BOUND_HEIGHT = MeasureHelper.SCREEN_HEIGHT * 0.074074f;
    private static final float SELECT_BOUND_PADDING = MeasureHelper.SCREEN_HEIGHT * 0.0125f;
    private static final float VIEW_WIDTH = MeasureHelper.SCREEN_WIDTH * 0.745833f;
    private static final float SPACING = MeasureHelper.SCREEN_WIDTH * 0.046875f;
    private static final float FONT_SIZE = 48f;

    private Paint paint;
    private RectF textBound;
    private Rect selectedBound;
    private Scroller scroller;

    private List<String> candidates;
    private float currentBoundX, textHeight, totalLength, scrollLimitX, scrolledLength;
    private int selectIndex;
    private float[] wordsX;
    private boolean cursorAlive;

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

        selectedBound = new Rect();
        textBound = new RectF();
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
            paint.setColor(Color.RED);
            canvas.drawLine(VIEW_WIDTH / 2, 0, VIEW_WIDTH / 2, getHeight(), paint);
        }
    }

    private void drawCandidates(Canvas canvas) {
        for (int index = 0, size = candidates.size(); index < size; index++) {
            String text = candidates.get(index);
            float textWidth = paint.measureText(text);

            calTextBound(textWidth);
            if (index == selectIndex && cursorAlive) {
                drawSelectBound(canvas);
                selectedBound.set((int) textBound.left, (int) textBound.top,
                        (int) textBound.right, (int) textBound.bottom);
            }
            drawCandidateWord(canvas, text);
            wordsX[index] = currentBoundX;
            totalLength = currentBoundX + SPACING * 2 + textWidth;
            currentBoundX += SPACING + textWidth;

        }
    }

    private void calTextBound(float textWidth) {
        float left = currentBoundX;
        float top = (getHeight() - SELECT_BOUND_HEIGHT) / 2;
        float right = currentBoundX + textWidth + SELECT_BOUND_PADDING * 2;
        float bottom = top + SELECT_BOUND_HEIGHT;
        textBound.set(left, top, right, bottom);
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
        canvas.drawRect(textBound, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.default_soft_key_stroke));
        canvas.drawRect(textBound, paint);
    }

    public void cursorForward() {
        int nextIndex = selectIndex + 1;
        if (nextIndex < candidates.size()) {
            scrollText(nextIndex);
            selectIndex = nextIndex;
            invalidate();
        }
    }

    private void scrollText(int nextIndex) {
        int currX = (int) wordsX[selectIndex];
        int nextX = (int) wordsX[nextIndex];
        float currWidth = paint.measureText(candidates.get(selectIndex));
        float nextWidth = paint.measureText(candidates.get(nextIndex));
        float currCenter = currX + currWidth / 2;
        float nextCenter = nextX + nextWidth / 2;
        float scrollLimitX = currentBoundX - VIEW_WIDTH;
        float dx = nextCenter - currCenter;
        float resultScrollX = scrolledLength + dx;
        if (resultScrollX < 0) {
            dx = 0 - scrolledLength;
        } else if (resultScrollX >= scrollLimitX){
            dx = scrollLimitX - scrolledLength;
        }

        if (VIEW_WIDTH / 2 <= nextCenter && nextCenter < totalLength - VIEW_WIDTH / 2){
            scrollBy((int) dx, 0);
            scrolledLength += dx;
        }
    }


    public void cursorBackward() {
        int nextIndex = selectIndex - 1;
        if (nextIndex >= 0) {
            scrollText(nextIndex);
            selectIndex = nextIndex;
            invalidate();
        }
    }

    public String selectCandidate() {
        return candidates.get(selectIndex);
    }

    public void clean() {
        selectIndex = 0;
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
        scrollTo(0, 0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) VIEW_WIDTH, heightMeasureSpec);
    }
}
