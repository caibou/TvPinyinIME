package me.caibou.ime;

import me.caibou.ime.pattern.SoftKey;

/**
 * @author caibou
 */
public interface KeyboardListener {

    void onCommitText(String text);

    void onSoftKeyClick(SoftKey softKey);

}
