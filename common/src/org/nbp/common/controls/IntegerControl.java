package org.nbp.common.controls;
import org.nbp.common.*;

import android.content.SharedPreferences;

public abstract class IntegerControl extends Control {
  protected abstract int getIntegerDefault ();
  public abstract int getIntegerValue ();
  protected abstract boolean setIntegerValue (int value);

  protected int getIntegerScale () {
    return 1;
  }

  protected Integer getIntegerMinimum () {
    return null;
  }

  protected Integer getIntegerMaximum () {
    return null;
  }

  private final boolean adjustValue (int adjustment) {
    int value = getIntegerValue();
    int scale = getIntegerScale();

    value += adjustment * scale;
    value /= scale;
    value *= scale;

    if (adjustment > 0) {
      Integer maximum = getIntegerMaximum();
      if ((maximum != null) && (value > maximum)) return false;
    } else if (adjustment < 0) {
      Integer minimum = getIntegerMinimum();
      if ((minimum != null) && (value < minimum)) return false;
    }

    return setIntegerValue(value);
  }

  @Override
  protected final boolean setNextValue () {
    return adjustValue(1);
  }

  @Override
  protected final boolean setPreviousValue () {
    return adjustValue(-1);
  }

  @Override
  protected final boolean setDefaultValue () {
    return setIntegerValue(getIntegerDefault());
  }

  public final boolean setValue (int value) {
    synchronized (this) {
      if (value == getIntegerValue()) return true;
      if (!setIntegerValue(value)) return false;
      reportValueChange();
    }

    return true;
  }

  @Override
  public CharSequence getValue () {
    return Integer.toString(getIntegerValue());
  }

  @Override
  protected int getResourceForNext () {
    return R.string.control_next_numeric;
  }

  @Override
  protected int getResourceForPrevious () {
    return R.string.control_previous_numeric;
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putInt(key, getIntegerValue());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs, String key) {
    return setIntegerValue(prefs.getInt(key, getIntegerDefault()));
  }

  protected IntegerControl () {
    super();
  }
}
