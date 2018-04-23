package me.caibou.ime;

import android.content.Context;
import android.graphics.Paint;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author caibou
 */
public class MeasureHelper {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static void measure(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        SCREEN_WIDTH = display.getWidth();
        SCREEN_HEIGHT = display.getHeight();
    }

    public static float getFontHeight(Paint paint){
        Paint.FontMetrics fm = paint.getFontMetrics();
        return ((int) Math.ceil(fm.descent - fm.top) + 2) / 2;
    }
}
