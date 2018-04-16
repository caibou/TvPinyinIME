package me.caibou.ime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caibou
 */
public class KeyRow {

    private List<SoftKey> keys = new ArrayList<>();

    public SoftKey getKey(int index) {
        return keys.get(index);
    }

    public void addKey(SoftKey key) {
        keys.add(key);
    }

    public int keyCount() {
        return keys.size();
    }

}
