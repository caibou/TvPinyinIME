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
import me.caibou.ime.R;

/**
 * @author caibou
 */
public class CandidatesView extends View {

    private static final float SELECT_BOUND_HEIGHT = MeasureHelper.SCREEN_HEIGHT * 0.074074f;
    private static final float SELECT_BOUND_PADDING = MeasureHelper.SCREEN_HEIGHT * 0.0125f;
    private static final float VIEW_HEIGHT = MeasureHelper.SCREEN_HEIGHT * 0.087037f;
    private static final float WORD_SPACING = MeasureHelper.SCREEN_WIDTH * 0.046875f;
    private static final float FONT_SIZE = 48f;

    private Paint paint;
    private RectF selectBound;
    private Scroller scroller;

    private List<String> candidates;
    private float leftPadding, currentTextX;
    private int selectIndex;

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
        selectBound = new RectF();

        leftPadding = MeasureHelper.SCREEN_WIDTH * 0.123958f;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int backgroundColor = getResources().getColor(R.color.keyboard_background_color);
        canvas.drawColor(backgroundColor);
        currentTextX = leftPadding;
        if (candidates.isEmpty()) {
            // TODO: 2018/4/23 Draw tab
        } else {
            drawCandidates(canvas);
        }
    }

    private void drawCandidates(Canvas canvas) {
        paint.setTextSize(FONT_SIZE);
        float textHeight = MeasureHelper.getFontHeight(paint);
        for (int index = 0, size = candidates.size(); index < size; index++) {
            String text = candidates.get(index);
            float textWidth = paint.measureText(text);
            if (index == selectIndex) {
                drawSelectBound(canvas, textWidth);
            }
            drawCandidateWord(canvas, textHeight, text);
            currentTextX += WORD_SPACING + textWidth;
        }
    }

    private void drawCandidateWord(Canvas canvas, float textHeight, String text) {
        paint.reset();
        paint.setTextSize(FONT_SIZE);
        paint.setColor(getResources().getColor(R.color.default_soft_key_label));
        canvas.drawText(text, currentTextX, (VIEW_HEIGHT + textHeight) / 2, paint);
    }

    private void drawSelectBound(Canvas canvas, float textWidth) {
        float left = currentTextX - SELECT_BOUND_PADDING;
        float top = (VIEW_HEIGHT - SELECT_BOUND_HEIGHT) / 2;
        float right = currentTextX + textWidth + SELECT_BOUND_PADDING;
        float bottom = top + SELECT_BOUND_HEIGHT;
        selectBound.set(left, top, right, bottom);

        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.default_key_selected_bg));
        canvas.drawRect(selectBound, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.default_soft_key_stroke));
        canvas.drawRect(selectBound, paint);
    }

    public void cursorForward() {

    }

    public void cursorBackward() {

    }

    public void clean() {
        candidates.clear();
        invalidate();
    }

    public void updateCandidates(List<String> candidates) {
        this.candidates = candidates;
        selectIndex = 0;
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
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.AT_MOST);
        setMeasuredDimension(widthMeasureSpec, (int) VIEW_HEIGHT);
    }
}
