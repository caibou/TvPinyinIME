package me.caibou.ime;

import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author caibou
 */
public class MeasureHelper {

    public static int KEYBOARD_WIDTH;
    public static int KEYBOARD_HEIGHT;
    public static int CANDIDATE_VIEW_HEIGHT;

    public static void measure(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        DisplayMetrics dm = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        KEYBOARD_WIDTH = dm.widthPixels;
        CANDIDATE_VIEW_HEIGHT = (int) (KEYBOARD_WIDTH * 0.048958f);
        KEYBOARD_HEIGHT = (int) (KEYBOARD_WIDTH * 0.178125f);
    }

    public static float getFontHeight(Paint paint){
        Paint.FontMetrics fm = paint.getFontMetrics();
        return ((int) Math.ceil(fm.descent - fm.top) + 2) / 2;
    }
}
