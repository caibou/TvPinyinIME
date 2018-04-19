package me.caibou.ime;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.regex.Pattern;

import me.caibou.ime.pattern.Element;
import me.caibou.ime.pattern.SoftKey;

/**
 * @author caibou
 */
public class KeyboardLoader {

    private static final String TAG = "KeyboardLoader";

    private static final String TAG_KEYBOARD = "keyboard";
    private static final String TAG_ROW = "row";
    private static final String TAG_KEYS = "keys";
    private static final String TAG_KEY = "key";

    private static final String ATTR_KEYBOARD_BACKGROUND_COLOR = "keyboard_background_color";
    private static final String ATTR_DEF_KEY_TEXT_COLOR = "default_soft_key_label_color";
    private static final String ATTR_DEF_KEY_NORMAL_COLOR = "soft_key_normal_bg_color";
    private static final String ATTR_DEF_KEY_STROKE_COLOR = "default_soft_key_stroke_color";
    private static final String ATTR_DEF_KEY_SELECTED_COLOR = "default_soft_key_selected_bg_color";
    private static final String ATTR_DEF_KEY_PRESSED_COLOR = "default_soft_key_pressed_bg_color";

    private static final String ATTR_DEF_KEY_WIDTH = "default_soft_key_width";
    private static final String ATTR_DEF_KEY_HEIGHT = "default_soft_key_height";
    private static final String ATTR_DEF_KEY_STROKE_WIDTH = "default_soft_key_stroke_width";
    private static final String ATTR_DEF_KEY_ICON_SIZE = "default_soft_key_icon_size";

    private static final String ATTR_DEF_KEYS_SPACING = "default_soft_keys_spacing";
    private static final String ATTR_DEF_POS_X = "default_position_x";
    private static final String ATTR_DEF_POS_Y = "default_position_y";

    private static final String ATTR_LABEL = "label";
    private static final String ATTR_LABELS = "labels";
    private static final String ATTR_KEYCODE = "keycode";
    private static final String ATTR_CODES = "codes";
    private static final String ATTR_SPLITTER = "splitter";
    private static final String ATTR_KEY_TEXT_SIZE = "text_size";
    private static final String ATTR_ICON = "icon";

    private static final String ATTR_WIDTH = "width";
    private static final String ATTR_HEIGHT = "height";
    private static final String ATTR_POS_X = "pos_x";
    private static final String ATTR_POS_Y = "pos_y";

    private static final String ATTR_CROSS_ROW = "cross_row";
    private static final String ATTR_CROSS_COLUMNS = "cross_columns";
    private static final String ATTR_TEXT_COLOR = "text_color";
    private static final String ATTR_ICON_WIDTH = "icon_width";
    private static final String ATTR_ICON_HEIGHT = "icon_height";
    private static final String ATTR_STROKE_COLOR = "stroke_color";
    private static final String ATTR_STROKE_WIDTH = "stroke_width";
    private static final String ATTR_NORMAL_BG_COLOR = "normal_bg_color";
    private static final String ATTR_PRESSED_COLOR = "pressed_color";
    private static final String ATTR_SELECTED_COLOR = "selected_color";
    private static final String ATTR_LABEL_ORIENTATION = "label_orientation";

    private Resources resources;
    private int defKeyTextColor, defKeyNormalColor, defKeyStrokeColor, defKeySelectedColor, defPressedColor;
    private float defKeyStrokeWidth, defKeyIconSize, defTextSize;
    private float defKeyWidth, defKeyHeight, defPosX, defPosY, defKeySpacing;

    private float nextPosX, nextPosY;

    public KeyboardLoader(@NonNull Context context) {
        resources = context.getResources();
    }

    public SoftKeyboard load(int keyboardId) {
        SoftKeyboard softKeyboard = new SoftKeyboard();
        XmlResourceParser xmlParser = resources.getXml(keyboardId);
        try {
            int event = xmlParser.next();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlResourceParser.START_TAG) {
                    parseElementStartTag(xmlParser, softKeyboard);
                } else if (event == XmlResourceParser.END_TAG) {
                    parseElementEndTag(xmlParser, softKeyboard);
                }
                event = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            xmlParser.close();
        }
        return softKeyboard;
    }

    private void parseElementEndTag(XmlResourceParser xmlParser, SoftKeyboard softKeyboard) {
        String attr = xmlParser.getName().toLowerCase();
        switch (attr) {
            case TAG_ROW:
                nextPosX = defPosX;
                nextPosY += (defKeySpacing + 80);
                break;
        }
    }

    private void parseElementStartTag(XmlResourceParser xmlParser, @NonNull SoftKeyboard softKeyboard) {
        String attr = xmlParser.getName().toLowerCase();
        switch (attr) {
            case TAG_KEYBOARD:
                loadDefaultConfig(xmlParser);
                loadKeyboard(xmlParser, softKeyboard);
                nextPosX = defPosX;
                nextPosY = defPosY;
                break;
            case TAG_ROW:
                loadRow(softKeyboard);
                break;
            case TAG_KEYS:
                loadKeys(xmlParser, softKeyboard);
                break;
            case TAG_KEY:
                loadSingleKey(xmlParser, softKeyboard);
                break;
        }
    }

    private void loadDefaultConfig(XmlResourceParser xmlParser) {
        defKeyTextColor = XmlParseUtil.loadColor(resources, xmlParser, ATTR_DEF_KEY_TEXT_COLOR,
                resources.getColor(R.color.default_soft_key_label));
        defKeyNormalColor = XmlParseUtil.loadColor(resources, xmlParser, ATTR_DEF_KEY_NORMAL_COLOR,
                resources.getColor(R.color.default_soft_key_normal_bg));
        defKeyStrokeColor = XmlParseUtil.loadColor(resources, xmlParser, ATTR_DEF_KEY_STROKE_COLOR,
                resources.getColor(R.color.default_soft_key_stroke));
        defKeySelectedColor = XmlParseUtil.loadColor(resources, xmlParser, ATTR_DEF_KEY_SELECTED_COLOR,
                resources.getColor(R.color.default_key_selected_bg));
        defPressedColor = XmlParseUtil.loadColor(resources, xmlParser, ATTR_DEF_KEY_PRESSED_COLOR,
                resources.getColor(R.color.default_key_pressed_bg));
        defKeyWidth = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_DEF_KEY_WIDTH,
                resources.getDimension(R.dimen.default_soft_key_width));
        defKeyHeight = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_DEF_KEY_HEIGHT,
                resources.getDimension(R.dimen.default_soft_key_height));
        defPosX = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_DEF_POS_X,
                resources.getDimension(R.dimen.default_soft_key_height));
        defPosY = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_DEF_POS_Y,
                resources.getDimension(R.dimen.default_soft_key_height));
        defKeyStrokeWidth = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_DEF_KEY_STROKE_WIDTH,
                resources.getDimension(R.dimen.default_soft_key_stroke_width));
        defKeyIconSize = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_DEF_KEY_ICON_SIZE,
                resources.getDimension(R.dimen.default_soft_key_icon_size));
        defTextSize = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_KEY_TEXT_SIZE,
                resources.getDimension(R.dimen.default_soft_key_text_size));
    }

    private void loadKeyboard(XmlResourceParser xmlParser, SoftKeyboard keyboard) {
        keyboard.setBackgroundColor(
                XmlParseUtil.loadColor(resources, xmlParser, ATTR_KEYBOARD_BACKGROUND_COLOR,
                        R.color.keyboard_background_color));

        defKeySpacing = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_DEF_KEYS_SPACING,
                resources.getDimension(R.dimen.default_soft_keys_spacing));

        keyboard.setKeysSpacing(defKeySpacing);
    }

    private void loadRow(@NonNull SoftKeyboard softKeyboard) {
        KeyRow keyRow = new KeyRow();
        softKeyboard.addKeyRow(keyRow);
    }

    private void loadKeys(XmlResourceParser xmlParser, SoftKeyboard softKeyboard) {
        String splitter = Pattern.quote(XmlParseUtil.loadString(resources, xmlParser, ATTR_SPLITTER));
        if (!TextUtils.isEmpty(splitter)) {
            String[] keys = XmlParseUtil.loadString(resources, xmlParser, ATTR_CODES).split(splitter);
            String[] labels = XmlParseUtil.loadString(resources, xmlParser, ATTR_LABELS).split(splitter);
            for (int i = 0, size = keys.length; i < size; i++) {
                SoftKey softKey = getSoftKey(xmlParser);
                softKey.setKeyLabel(labels[i]);
                softKey.setKeyCode(Integer.parseInt(keys[i]));
                softKeyboard.addSoftKey(softKey);
            }
        }
    }

    private void loadSingleKey(XmlResourceParser xmlParser, @NonNull SoftKeyboard softKeyboard) {
        SoftKey softKey = getSoftKey(xmlParser);
        softKeyboard.addSoftKey(softKey);
    }

    private void loadPattern(Element element, XmlResourceParser xmlParser) {
        float width = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_WIDTH, defKeyWidth);
        float height = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_HEIGHT, defKeyHeight);
        float left = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_POS_X, nextPosX);
        float top = XmlParseUtil.loadDimen(resources, xmlParser, ATTR_POS_Y, nextPosY);

        element.setWidth(width);
        element.setHeight(height);
        element.setLeft(left);
        element.setTop(top);
    }

    @NonNull
    private SoftKey getSoftKey(XmlResourceParser xmlParser) {
        SoftKey softKey = new SoftKey();
        softKey.setKeyCode(XmlParseUtil.loadInt(
                resources, xmlParser, ATTR_KEYCODE, KeyEvent.KEYCODE_UNKNOWN));
        softKey.setTextColor(XmlParseUtil.loadColor(
                resources, xmlParser, ATTR_TEXT_COLOR, defKeyTextColor));
        softKey.setTextSize(XmlParseUtil.loadDimen(
                resources, xmlParser, ATTR_KEY_TEXT_SIZE, defTextSize));
        softKey.setKeyLabel(XmlParseUtil.loadString(
                resources, xmlParser, ATTR_LABEL));
        softKey.setCrossColumn(XmlParseUtil.loadInt(
                resources, xmlParser, ATTR_CROSS_COLUMNS, 1));
        softKey.setCrossRow(XmlParseUtil.loadInt(
                resources, xmlParser, ATTR_CROSS_ROW, 1));
        softKey.setIconWidth(XmlParseUtil.loadDimen(
                resources, xmlParser, ATTR_ICON_WIDTH, defKeyIconSize));
        softKey.setIconHeight(XmlParseUtil.loadDimen(
                resources, xmlParser, ATTR_ICON_HEIGHT, defKeyIconSize));
        softKey.setNormalColor(XmlParseUtil.loadColor(
                resources, xmlParser, ATTR_NORMAL_BG_COLOR, defKeyNormalColor));
        softKey.setPressedColor(XmlParseUtil.loadColor(
                resources, xmlParser, ATTR_PRESSED_COLOR, defPressedColor));
        softKey.setSelectedColor(XmlParseUtil.loadColor(
                resources, xmlParser, ATTR_SELECTED_COLOR, defKeySelectedColor));
        softKey.setStrokeColor(XmlParseUtil.loadColor(
                resources, xmlParser, ATTR_STROKE_COLOR, defKeyStrokeColor));
        softKey.setStrokeWidth(XmlParseUtil.loadDimen(
                resources, xmlParser, ATTR_STROKE_WIDTH, defKeyStrokeWidth));
        softKey.setOrientation(XmlParseUtil.loadInt(
                resources, xmlParser, ATTR_LABEL_ORIENTATION, SoftKey.HORIZONTAL));
        softKey.setIcon(XmlParseUtil.loadDrawable(
                resources, xmlParser, ATTR_ICON));
        loadPattern(softKey, xmlParser);
        fixKeyRect(softKey);
        calNextPosX(softKey);
        return softKey;
    }

    private void calNextPosX(Element element) {
        nextPosX = element.getRight() + defKeySpacing;
    }

    private void fixKeyRect(SoftKey softKey) {
        int crossRow = softKey.getCrossRow();
        int crossColumn = softKey.getCrossColumn();
        float width = softKey.getWidth() * crossColumn + defKeySpacing * (crossColumn - 1);
        float height = softKey.getHeight() * crossRow + defKeySpacing * (crossRow - 1);
        softKey.setWidth(width);
        softKey.setHeight(height);
    }

}
