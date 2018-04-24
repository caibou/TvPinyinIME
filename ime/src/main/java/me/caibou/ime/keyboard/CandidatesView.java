package me.caibou.ime.keyboard;

import android.content.Context;
import android.graphics.Canvas;
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
    private float currentBoundX, textHeight;
    private int selectIndex;
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
        if (selectIndex + 1 < candidates.size()) {
            selectIndex++;
            invalidate();
        }
    }

    public void cursorBackward() {
        if (selectIndex - 1 >= 0) {
            selectIndex--;
            invalidate();
        }
    }

    public void cancelSelected() {
        cursorAlive = false;
        invalidate(selectedBound);
    }

    public String selectCandidate() {
        return candidates.get(selectIndex);
    }

    public void clean() {
        selectIndex = 0;
        candidates.clear();
        invalidate();
    }

    public void setCursorAlive(boolean cursorAlive){
        this.cursorAlive = cursorAlive;
    }

    public boolean isCursorAlive(){
        return cursorAlive;
    }

    public void updateCandidates(List<String> candidates) {
        this.candidates.clear();
        this.candidates.addAll(candidates);
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
