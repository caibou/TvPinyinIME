package me.caibou.ime;

import android.inputmethodservice.InputMethodService;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;

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
        return skbContainer;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isImeServiceStop()){
            if (skbContainer.onSoftKeyDown(keyCode, event)){
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!isImeServiceStop()){
            if (skbContainer.onSoftKeyUp(keyCode, event)){
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public void commitResultText(String resultText) {
        InputConnection ic = getCurrentInputConnection();
        if (null != ic && !TextUtils.isEmpty(resultText)) {
            ic.commitText(resultText, 1);
        }
    }

    public boolean isImeServiceStop() {
        return skbContainer == null || !isInputViewShown();
    }


    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }
}
