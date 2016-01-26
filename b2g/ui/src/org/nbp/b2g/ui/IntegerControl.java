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
  public CharSequence getNextLabel () {
    return getString(R.string.numeric_control_next);
  }

  @Override
  public CharSequence getPreviousLabel () {
    return getString(R.string.numeric_control_previous);
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putInt(key, getIntegerValue());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs, String key) {
    return setIntegerValue(prefs.getInt(key, getIntegerDefault()));
  }

  protected IntegerControl (boolean isForDevelopers) {
    super(isForDevelopers);
  }
}
