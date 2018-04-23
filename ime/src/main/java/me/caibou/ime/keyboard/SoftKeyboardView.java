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

        canvas.drawColor(getResources().getColor(softKeyboard.getBackgroundColor()));
        isUpperCase = InputMethodSwitcher.getInstance().isUpperCase();
        for (int rowIndex = 0, rowNum = softKeyboard.getRowNum(); rowIndex < rowNum; rowIndex++) {

            KeyRow keyRow = softKeyboard.getRow(rowIndex);
            for (int keyIndex = 0, size = keyRow.keyCount(); keyIndex < size; keyIndex++) {
                SoftKey softKey = keyRow.getKey(keyIndex);
                softKey.setSelected(rowIndex == softKeyboard.getSelectRow() &&
                        keyIndex == softKeyboard.getSelectIndex() && softKeyboard.isSelected());
                drawSoftKey(canvas, softKey);
            }
        }
    }

    private void drawSoftKey(Canvas canvas, SoftKey softKey) {

        rectF.set(softKey.getLeft(), softKey.getTop(), softKey.getRight(), softKey.getBottom());

        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        int keyBackgroundColor = softKey.isPressed() ? softKey.getPressedColor() :
                softKey.isSelected() ? softKey.getSelectedColor() : softKey.getNormalColor();
        paint.setColor(keyBackgroundColor);
        canvas.drawRect(rectF, paint);

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(softKey.getStrokeWidth());
        paint.setColor(softKey.getStrokeColor());
        canvas.drawRect(rectF, paint);

        if (softKey.getIcon() != null) {
            Drawable icon = softKey.getIcon();
            int left = (int) (rectF.centerX() - softKey.getIconWidth() / 2);
            int top = (int) (rectF.centerY() - softKey.getIconHeight() / 2);
            int right = (int) (left + softKey.getIconWidth());
            int bottom = (int) (top + softKey.getIconHeight());
            icon.setBounds(left, top, right, bottom);
            icon.draw(canvas);
        }

        if (!TextUtils.isEmpty(softKey.getKeyLabel())) {
            String label = softKey.getKeyLabel();
            if (isUpperCase){
                label = label.toUpperCase();
            }
            paint.reset();
            paint.setColor(softKey.getTextColor());
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(softKey.getTextSize());
            paint.setAntiAlias(true);

            paint.getTextBounds(label, 0, label.length(), labelBound);

            float x = rectF.centerX() - labelBound.width() / 2;
            float y = rectF.centerY() + labelBound.height() / 2;
            canvas.drawText(label, x, y, paint);
        }

    }

}
