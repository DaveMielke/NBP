package org.nbp.common;

public class BitSet {
  public final int size;

  protected final static int BITS_PER_ELEMENT = 8;
  protected final byte[] elements;

  private final int getElementCount () {
    return (size + (BITS_PER_ELEMENT - 1)) / BITS_PER_ELEMENT;
  }

  public final boolean empty () {
    boolean changed = false;

    for (int index=0; index<elements.length; index+=1) {
      if (elements[index] != 0) {
        elements[index] = 0;
        changed = true;
      }
    }

    return changed;
  }

  public BitSet (int size) {
    this.size = size;
    elements = new byte[getElementCount()];
    empty();
  }

  private final int getIndex (int member) {
    return member / BITS_PER_ELEMENT;
  }

  private final byte getBit (int member) {
    return (byte)(1 << (member % BITS_PER_ELEMENT));
  }

  private final boolean test (int index, byte bit) {
    return (elements[index] & bit) != 0;
  }

  public final boolean test (int member) {
    int index = getIndex(member);
    byte bit = getBit(member);
    return test(index, bit);
  }

  public final boolean add (int member) {
    int index = getIndex(member);
    byte bit = getBit(member);

    if (test(index, bit)) return false;
    elements[index] |= bit;
    return true;
  }

  public final boolean remove (int member) {
    int index = getIndex(member);
    byte bit = getBit(member);

    if (!test(index, bit)) return false;
    elements[index] &= ~bit;
    return true;
  }

  public final boolean set (int member, boolean state) {
    return state? add(member): remove(member);
  }
}
