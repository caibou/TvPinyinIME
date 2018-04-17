package me.caibou.ime;

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

    float currentTop = 124, currentLeft = 213;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (softKeyboard == null) {
            return;
        }

        canvas.drawColor(getResources().getColor(softKeyboard.getBackgroundColor()));

        for (int rowIndex = 0, rowNum = softKeyboard.getRowNum(); rowIndex < rowNum; rowIndex++) {

            KeyRow keyRow = softKeyboard.getRow(rowIndex);
            for (int keyIndex = 0, size = keyRow.keyCount(); keyIndex < size; keyIndex++) {
                SoftKey softKey = keyRow.getKey(keyIndex);
                drawSoftKey(canvas, softKey);
            }
            currentTop += 98;
            currentLeft = 213;
        }
    }

    private void drawSoftKey(Canvas canvas, SoftKey softKey) {

        int crossRow = softKey.getCrossRow();
        int crossColumn = softKey.getCrossColumn();
        float spacing = softKeyboard.getKeysSpacing();
        float width = softKey.getWidth() * crossColumn + spacing * (crossColumn - 1);
        float height = softKey.getHeight() * crossRow + spacing * (crossRow - 1);
        rectF.set(currentLeft, currentTop, currentLeft + width, currentTop + height);

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

        if (softKey.getIcon() != null){
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
            paint.reset();
            paint.setColor(softKey.getTextColor());
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(softKey.getTextSize());
            paint.setAntiAlias(true);


            Rect bound = new Rect();
            paint.getTextBounds(label, 0, label.length(), bound);

            float x = rectF.centerX() - bound.width() / 2;
            float y = rectF.centerY() + bound.height() / 2;
            canvas.drawText(label, x, y, paint);
        }


        currentLeft = currentLeft + width + spacing;
        
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(1920, 436);
    }
}
