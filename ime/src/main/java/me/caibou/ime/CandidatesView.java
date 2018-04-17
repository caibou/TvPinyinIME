package me.caibou.ime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caibou
 */
public class CandidatesView extends View {

    private Scroller scroller;

    private List<String> candidates;

    private boolean expanded;
    private float wordSpacing, leftPadding, moreIconLeft, moreIconWidth, moreIconHeight;

    private Paint paint;

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
        paint.setColor(Color.WHITE);

        wordSpacing = MeasureData.sScreenWidth * 0.083333f;
        leftPadding = MeasureData.sScreenWidth * 0.220370f;
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    public void cursorForward(){

    }

    public void cursorBackward(){

    }

    public void expand(){
        expanded = true;
    }

    public void collapse(){
        expanded = false;
    }

    public boolean isExpand(){
        return expanded;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }
}
