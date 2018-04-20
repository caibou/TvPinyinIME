package me.caibou.ime;

import android.content.Context;
import android.graphics.Paint;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author caibou
 */
public class MeasureHelper {

    public static int sScreenWidth;
    public static int sScreenHeight;

    public static void measure(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        sScreenWidth = display.getWidth();
        sScreenHeight = display.getHeight();
    }

    public static float getFontHeight(Paint paint){
        Paint.FontMetrics fm = paint.getFontMetrics();
        return ((int) Math.ceil(fm.descent - fm.top) + 2) / 2;
    }
}
