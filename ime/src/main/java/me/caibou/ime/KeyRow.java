package me.caibou.ime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caibou
 */
public class KeyRow {

    private List<SoftKey> keys = new ArrayList<>();

    public List<SoftKey> getKeys() {
        return keys;
    }

    public void addKey(SoftKey key) {
        keys.add(key);
    }

}
