package me.caibou.ime;

import android.inputmethodservice.InputMethodService;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author caibou
 */
public class ImeService extends InputMethodService {

    private static final String TAG = "ImeService";

    private SkbContainer skbContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        MeasureData.measure(getApplicationContext());
    }

    @Override
    public View onCreateInputView() {
        LayoutInflater inflater = getLayoutInflater();
        skbContainer = (SkbContainer) inflater.inflate(R.layout.layout_skb_container, null);
        SoftKeyboardView softKeyboardView = skbContainer.findViewById(R.id.keyboard_view);
        softKeyboardView.setSoftKeyboard(new KeyboardLoader(getApplicationContext()).load(R.xml.skb_qwerty_en));
        return skbContainer;
    }

    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }
}
