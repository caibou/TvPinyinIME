package me.caibou.ime;

/**
 * @author caibou
 */
public class InputMethodSwitcher {

    private static InputMethodSwitcher INSTANCE;

    private int keyboardLayoutId;
    private boolean isUpperCase;

    private InputMethodSwitcher(){};

    public static InputMethodSwitcher getInstance() {
        if (INSTANCE == null){
            INSTANCE = new InputMethodSwitcher();
        }
        return INSTANCE;
    }

    public void setKeyboardLayoutId(int keyboardLayoutId){
        this.keyboardLayoutId = keyboardLayoutId;
    }

    public boolean isChineseText(){
        return keyboardLayoutId == R.xml.skb_qwerty_cn;
    }

    public boolean isUpperCase() {
        return isUpperCase;
    }

    public void setUpperCase(boolean upperCase) {
        isUpperCase = upperCase;
    }
}
