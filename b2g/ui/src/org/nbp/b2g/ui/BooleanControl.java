package org.nbp.b2g.ui;

import android.content.SharedPreferences;

public abstract class BooleanControl extends Control {
  protected abstract boolean getBooleanValue ();
  protected abstract boolean setBooleanValue (boolean value);

  protected boolean getBooleanDefault () {
    return false;
  }

  @Override
  public boolean next () {
    return setBooleanValue(true);
  }

  @Override
  public boolean previous () {
    return setBooleanValue(false);
  }

  @Override
  public String getValue () {
    return Boolean.toString(getBooleanValue());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs) {
    return setBooleanValue(prefs.getBoolean(getPreferenceKey(), getBooleanDefault()));
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor) {
    editor.putBoolean(getPreferenceKey(), getBooleanValue());
  }

  protected BooleanControl () {
    super();
  }
}
