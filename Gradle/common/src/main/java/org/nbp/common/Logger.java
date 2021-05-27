package org.nbp.common;

public abstract class Logger {
  public interface Iterator {
    public String getText ();
    public boolean next ();
  }

  private final Iterator logIterator;

  protected Logger (Iterator iterator) {
    logIterator = iterator;
  }

  protected boolean begin () {
    return true;
  }

  protected boolean end () {
    return true;
  }

  protected abstract boolean write (String text);

  public final boolean log () {
    if (!begin()) return false;
    boolean ok = true;

    while (logIterator.next()) {
      if (!write(logIterator.getText())) {
        ok = false;
        break;
      }
    }

    if (!end()) ok = false;
    return ok;
  }
}
