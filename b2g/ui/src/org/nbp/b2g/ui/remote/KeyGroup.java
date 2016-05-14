package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

public class KeyGroup {
  protected final int count;

  protected final static int KEYS_PER_ELEMENT = 8;
  protected final byte[] mask;

  private final int getMaskSize () {
    return (count + (KEYS_PER_ELEMENT - 1)) / KEYS_PER_ELEMENT;
  }

  public final boolean clear () {
    boolean changed = false;

    for (int index=0; index<mask.length; index+=1) {
      if (mask[index] != 0) {
        mask[index] = 0;
        changed = true;
      }
    }

    return changed;
  }

  public KeyGroup (int count) {
    this.count = count;

    mask = new byte[getMaskSize()];
    clear();
  }

  private final int getIndex (int key) {
    return key / KEYS_PER_ELEMENT;
  }

  private final byte getBit (int key) {
    return (byte)(1 << (key % KEYS_PER_ELEMENT));
  }

  private final boolean test (int index, byte bit) {
    return (mask[index] & bit) != 0;
  }

  public final boolean test (int key) {
    int index = getIndex(key);
    byte bit = getBit(key);
    return test(index, bit);
  }

  public final boolean press (int key) {
    int index = getIndex(key);
    byte bit = getBit(key);

    if (test(index, bit)) return false;
    mask[index] |= bit;
    return true;
  }

  public final boolean release (int key) {
    int index = getIndex(key);
    byte bit = getBit(key);

    if (!test(index, bit)) return false;
    mask[index] &= ~bit;
    return true;
  }

  public final boolean set (int key, boolean press) {
    return press? press(key): release(key);
  }
}
