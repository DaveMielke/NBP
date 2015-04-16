package org.nbp.b2g.ui;

import android.content.SharedPreferences;

public abstract class BooleanControl extends Control {
  protected abstract boolean getBooleanValue ();
  protected abstract boolean setBooleanValue (boolean value);

  protected boolean getBooleanDefault () {
    return false;
  }

  @Override
  public boolean setNextValue () {
    return setBooleanValue(true);
  }

  @Override
  public boolean setPreviousValue () {
    return setBooleanValue(false);
  }

  @Override
  public boolean setDefaultValue () {
    return setBooleanValue(getBooleanDefault());
  }

  @Override
  public String getValue () {
    return Boolean.toString(getBooleanValue());
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putBoolean(key, getBooleanValue());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs, String key) {
    return setBooleanValue(prefs.getBoolean(key, getBooleanDefault()));
  }

  protected BooleanControl () {
    super();
  }
}
