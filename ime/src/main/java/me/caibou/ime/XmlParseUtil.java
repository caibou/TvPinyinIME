package me.caibou.ime;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.text.TextUtils;

/**
 * @author caibou
 */
public class XmlParseUtil {

    public static int loadInt(Resources res, XmlResourceParser parser, String attr, int defValue) {
        int resId = parser.getAttributeResourceValue(null, attr, defValue);
        if (resId == 0){
            String numStr = parser.getAttributeValue(null, attr);
            try {
                return Integer.parseInt(numStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            return Integer.parseInt(res.getString(resId));
        }
        return defValue;
    }

    public static int loadColor(Resources res, XmlResourceParser parser, String attr, int defValue) {
        int resId = parser.getAttributeResourceValue(null, attr, defValue);
        if (resId == 0){
            String colorStr = parser.getAttributeValue(null, attr);
            if (!TextUtils.isEmpty(colorStr) && colorStr.startsWith("#")){
                return Color.parseColor(colorStr);
            }
        } else {
            return res.getColor(resId);
        }
        return defValue;
    }

    public static String loadString(Resources res, XmlResourceParser parser, String attr) {
        int resId = parser.getAttributeResourceValue(null, attr, 0);
        if (resId == 0){
            return parser.getAttributeValue(null, attr);
        } else {
            return res.getString(resId);
        }
    }

    public static boolean loadBool(Resources res, XmlResourceParser parser, String attr, boolean defValue) {
        String boolString = parser.getAttributeValue(null, attr);
        if (!TextUtils.isEmpty(boolString)){
            return Boolean.parseBoolean(boolString);
        }
        return defValue;
    }
}
