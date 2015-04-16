package org.nbp.b2g.ui;

import android.content.SharedPreferences;

public abstract class IntegerControl extends Control {
  protected abstract int getIntegerDefault ();
  protected abstract int getIntegerValue ();
  protected abstract boolean setIntegerValue (int value);

  private boolean adjustValue (int steps) {
    int oldValue = getIntegerValue();
    int newValue = oldValue + steps;
    if (!setIntegerValue(newValue)) return false;
    reportValue();
    return true;
  }

  @Override
  public boolean setNextValue () {
    return adjustValue(1);
  }

  @Override
  public boolean setPreviousValue () {
    return adjustValue(-1);
  }

  @Override
  public boolean setDefaultValue () {
    return setIntegerValue(getIntegerDefault());
  }

  @Override
  public String getValue () {
    return Integer.toString(getIntegerValue());
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
