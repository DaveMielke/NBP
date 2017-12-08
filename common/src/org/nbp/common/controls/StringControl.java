package org.nbp.common.controls;
import org.nbp.common.*;

import java.util.Collection;

import android.util.Log;
import android.content.SharedPreferences;

public abstract class StringControl extends Control {
  private final static String LOG_TAG = StringControl.class.getName();

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

  protected boolean testStringValue (String value) {
    return true;
  }

  public final boolean setValue (String value) {
    synchronized (this) {
      {
        String currentValue = getStringValue();

        if (value == null) {
          if (currentValue == null) return true;
        } else if (value.equals(currentValue)) {
          return true;
        }
      }

      if (!testStringValue(value)) return false;
      if (!setStringValue(value)) return false;
      reportValueChange();
    }

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
    String value = getStringDefault();

    try {
      value = prefs.getString(key, value);
    } catch (ClassCastException exception) {
      Log.w(LOG_TAG, ("saved value not a string: " + getLabel()));
    }

    return setStringValue(value);
  }

  public Collection<String> getSuggestedValues () {
    return null;
  }

  protected StringControl () {
    super();
  }
}
