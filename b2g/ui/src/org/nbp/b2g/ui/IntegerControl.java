package org.nbp.b2g.ui;

import android.content.SharedPreferences;

public abstract class IntegerControl extends Control {
  protected abstract int getIntegerDefault ();
  public abstract int getIntegerValue ();
  protected abstract boolean setIntegerValue (int value);

  private final boolean adjustValue (int adjustment) {
    return setIntegerValue(getIntegerValue() + adjustment);
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
    if (!setIntegerValue(value)) return false;
    reportValue(false);
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

  protected IntegerControl (ControlGroup group) {
    super(group);
  }
}
