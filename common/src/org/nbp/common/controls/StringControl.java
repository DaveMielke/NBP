package org.nbp.common.controls;
import org.nbp.common.*;

import java.util.Collection;

import android.content.SharedPreferences;

public abstract class StringControl extends Control {
  protected abstract String getStringDefault ();
  public abstract String getStringValue ();
  protected abstract boolean setStringValue (String value);

  @Override
  protected final boolean setNextValue () {
    return false;
  }

  @Override
  protected final boolean setPreviousValue () {
    return false;
  }

  @Override
  protected final boolean setDefaultValue () {
    return setStringValue(getStringDefault());
  }

  public final boolean setValue (String value) {
    if (!setStringValue(value)) return false;
    reportValueChange();
    return true;
  }

  @Override
  public CharSequence getValue () {
    return getStringValue();
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putString(key, getStringValue());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs, String key) {
    return setStringValue(prefs.getString(key, getStringDefault()));
  }

  public Collection<String> getSuggestedValues () {
    return null;
  }

  protected StringControl () {
    super();
  }
}
