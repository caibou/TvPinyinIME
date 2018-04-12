package me.caibou.ime;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 软键盘View，主要是根据{@link SoftKeyboard}的信息绘制软键盘
 *
 * @author caibou
 */
public class SoftKeyboardView extends View {

    private SoftKeyboard softKeyboard;

    public SoftKeyboardView(Context context) {
        this(context, null);
    }

    public SoftKeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoftKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial(context);
    }

    public void setSoftKeyboard(SoftKeyboard softKeyboard) {
        this.softKeyboard = softKeyboard;
    }

    private void initial(Context context) {

    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
}
