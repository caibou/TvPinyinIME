package me.caibou.ime;

import android.inputmethodservice.InputMethodService;

/**
 * @author caibou
 */
public class ImeService extends InputMethodService {

    private static final String TAG = "ImeService";

    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }
}
