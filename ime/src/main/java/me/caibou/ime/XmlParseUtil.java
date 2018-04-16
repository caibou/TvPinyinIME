package me.caibou.ime;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * @author caibou
 */
public class XmlParseUtil {

    public static final int NON_VALUE = Integer.MIN_VALUE;

    public static int loadInt(Resources res, XmlResourceParser parser, String attr, int defValue) {
        int resId = parser.getAttributeResourceValue(null, attr, NON_VALUE);
        if (resId == NON_VALUE){
            String numStr = parser.getAttributeValue(null, attr);
            try {
                return Integer.parseInt(numStr);
            } catch (NumberFormatException e) {
                return defValue;
            }
        } else {
            return res.getInteger(resId);
        }
    }

    public static int loadColor(Resources res, XmlResourceParser parser, String attr, int defValue) {
        int resId = parser.getAttributeResourceValue(null, attr, NON_VALUE);
        if (resId != NON_VALUE){
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

    public static float loadDimen(Resources res, XmlResourceParser parser, String attr, float defValue){
        int resId = parser.getAttributeResourceValue(null, attr, NON_VALUE);
        if (resId != NON_VALUE){
            return res.getDimension(resId);
        }
        return defValue;
    }

    public static Drawable loadDrawable(Resources res, XmlResourceParser parser, String attr){
        int resId = parser.getAttributeResourceValue(null, attr, NON_VALUE);
        if (resId != NON_VALUE){
            return res.getDrawable(resId);
        }
        return null;
    }
}
