package me.caibou.ime.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import me.caibou.ime.InputMethodSwitcher;
import me.caibou.ime.pattern.KeyRow;
import me.caibou.ime.pattern.SoftKey;

/**
 * 软键盘View，主要是根据{@link SoftKeyboard}的信息绘制软键盘
 *
 * @author caibou
 */
public class SoftKeyboardView extends View {

    private static final String TAG = "SoftKeyboardView";

    private SoftKeyboard softKeyboard;

    private Paint paint;
    private RectF rectF;
    private Rect labelBound;

    private boolean isUpperCase;
    private boolean isCursorAlive = true;

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
        paint = new Paint();
        rectF = new RectF();
        labelBound = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (softKeyboard == null) {
            return;
        }

        canvas.drawColor(getResources().getColor(softKeyboard.backgroundColor));
        isUpperCase = InputMethodSwitcher.getInstance().isUpperCase();
        for (int rowIndex = 0, rowNum = softKeyboard.getRowNum(); rowIndex < rowNum; rowIndex++) {

            KeyRow keyRow = softKeyboard.getRow(rowIndex);
            for (int keyIndex = 0, size = keyRow.keyCount(); keyIndex < size; keyIndex++) {
                SoftKey softKey = keyRow.getKey(keyIndex);
                softKey.selected = (rowIndex == softKeyboard.selectRow &&
                        keyIndex == softKeyboard.selectIndex && isCursorAlive);
                drawSoftKey(canvas, softKey);
            }
        }
    }

    private void drawSoftKey(Canvas canvas, SoftKey softKey) {

        rectF.set(softKey.left, softKey.top, softKey.right, softKey.bottom);

        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        int keyBackgroundColor = softKey.selected ? softKey.selectedColor : softKey.normalColor;
        paint.setColor(keyBackgroundColor);
        canvas.drawRect(rectF, paint);

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(softKey.strokeWidth);
        paint.setColor(softKey.strokeColor);
        canvas.drawRect(rectF, paint);

        Drawable icon = softKey.icon;
        if (icon != null) {
            int left = (int) (rectF.centerX() - softKey.iconWidth / 2);
            int top = (int) (rectF.centerY() - softKey.iconHeight / 2);
            int right = (int) (left + softKey.iconWidth);
            int bottom = (int) (top + softKey.iconHeight);
            icon.setBounds(left, top, right, bottom);
            icon.draw(canvas);
        }

        String label = softKey.keyLabel;
        if (!TextUtils.isEmpty(label)) {
            if (isUpperCase) {
                label = label.toUpperCase();
            }
            paint.reset();
            paint.setColor(softKey.textColor);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(softKey.textSize);
            paint.setAntiAlias(true);

            paint.getTextBounds(label, 0, label.length(), labelBound);

            float x = rectF.centerX() - labelBound.width() / 2;
            float y = rectF.centerY() + labelBound.height() / 2;
            canvas.drawText(label, x, y, paint);
        }

    }

    public boolean isCursorAlive() {
        return isCursorAlive;
    }

    public void setCursorAlive(boolean cursorAlive) {
        isCursorAlive = cursorAlive;
        invalidate();
    }
}
