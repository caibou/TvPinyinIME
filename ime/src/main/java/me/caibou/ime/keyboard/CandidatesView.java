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

    private Scroller scroller;

    private List<String> candidates;

    private float wordSpacing, leftPadding, currentTextX;

    private Paint paint;

    private RectF selectBound;
    private Rect textBound;

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

        textBound = new Rect();
        selectBound = new RectF();

        wordSpacing = MeasureHelper.sScreenWidth * 0.046875f;
        leftPadding = MeasureHelper.sScreenWidth * 0.123958f;


        mockCandidates();
    }

    private void mockCandidates() {
        for (int i = 0; i < 20; i++) {
            candidates.add("哈");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int backgroundColor = getResources().getColor(R.color.keyboard_background_color);
        canvas.drawColor(backgroundColor);
        currentTextX = leftPadding;

        paint.setTextSize(48f);
        float textHeight = MeasureHelper.getFontHeight(paint);

        for (int index = 0, size = candidates.size(); index < size; index++) {
            String text = candidates.get(index);
            float textWidth = paint.measureText(text);
            if (index == selectIndex){
                drawSelectBound(canvas, textWidth);
            }
            drawCandidateWord(canvas, textHeight, text);
            currentTextX += wordSpacing + textWidth;
        }

    }

    private void drawSelectBound(Canvas canvas, float textWidth) {
        selectBound.set(currentTextX - 24, 7, currentTextX + textWidth + 24, 87);

        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.default_key_selected_bg));
        canvas.drawRect(selectBound, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.default_soft_key_stroke));
        canvas.drawRect(selectBound, paint);
    }

    private void drawCandidateWord(Canvas canvas, float textHeight, String text) {
        paint.reset();
        paint.setTextSize(48f);
        paint.setColor(getResources().getColor(R.color.default_soft_key_label));
        canvas.drawText(text, currentTextX, (getHeight() + textHeight) / 2, paint);
    }

    public void cursorForward() {

    }

    public void cursorBackward() {

    }

    public void updateCandidates(List<String> candidates) {
        this.candidates = candidates;
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
        setMeasuredDimension(1920, 94);
    }
}
