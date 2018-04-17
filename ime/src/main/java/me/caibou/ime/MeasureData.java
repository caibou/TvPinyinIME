package me.caibou.ime;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author caibou
 */
public class MeasureData {

    public static int sScreenWidth;
    public static int sScreenHeight;

    public static void measure(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        sScreenWidth = display.getWidth();
        sScreenHeight = display.getHeight();
    }
}
