package me.caibou.ime;

import android.inputmethodservice.InputMethodService;
import android.view.View;

/**
 * @author caibou
 */
public class ImeService extends InputMethodService {

    private static final String TAG = "ImeService";

    @Override
    public View onCreateInputView() {
        SoftKeyboardView softKeyboardView = new SoftKeyboardView(getApplicationContext());
        KeyboardLoader loader = new KeyboardLoader(getApplicationContext());
        softKeyboardView.setSoftKeyboard(loader.load(R.xml.skb_qwerty_en));
        return softKeyboardView;
    }

    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }
}
