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
  public boolean next () {
    return adjustValue(1);
  }

  @Override
  public boolean previous () {
    return adjustValue(-1);
  }

  @Override
  public String getValue () {
    return Integer.toString(getIntegerValue());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs) {
    return setIntegerValue(prefs.getInt(getPreferenceKey(), getIntegerDefault()));
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor) {
    editor.putInt(getPreferenceKey(), getIntegerValue());
  }

  protected IntegerControl () {
    super();
  }
}
