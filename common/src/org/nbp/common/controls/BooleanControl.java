package org.nbp.common.controls;
import org.nbp.common.*;

import android.util.Log;
import android.content.SharedPreferences;

public abstract class BooleanControl extends Control {
  private final static String LOG_TAG = BooleanControl.class.getName();

  public abstract boolean getBooleanValue ();
  protected abstract boolean setBooleanValue (boolean value);

  protected boolean getBooleanDefault () {
    return false;
  }

  protected boolean testBooleanValue (boolean value) {
    return true;
  }

  private final boolean changeValue (boolean value) {
    synchronized (this) {
      if (value == getBooleanValue()) return false;
      if (!testBooleanValue(value)) return false;
      return setBooleanValue(value);
    }
  }

  @Override
  protected final boolean setNextValue () {
    return changeValue(true);
  }

  @Override
  protected final boolean setPreviousValue () {
    return changeValue(false);
  }

  @Override
  protected final boolean setDefaultValue () {
    return setBooleanValue(getBooleanDefault());
  }

  public final boolean setValue (boolean value) {
    synchronized (this) {
      if (value == getBooleanValue()) return true;
      if (!testBooleanValue(value)) return false;
      if (!setBooleanValue(value)) return false;
      reportValueChange();
    }

    return true;
  }

  @Override
  public CharSequence getValue () {
    return getBooleanValue()? getLabelForNext(): getLabelForPrevious();
  }

  @Override
  protected int getResourceForNext () {
    return R.string.control_next_boolean;
  }

  @Override
  protected int getResourceForPrevious () {
    return R.string.control_previous_boolean;
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putBoolean(key, getBooleanValue());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs, String key) {
    boolean value = getBooleanDefault();

    try {
      value = prefs.getBoolean(key, value);
    } catch (ClassCastException exception) {
      Log.w(LOG_TAG, ("saved value not a boolean: " + getLabel()));
    }

    return setBooleanValue(value);
  }

  protected BooleanControl () {
    super();
  }
}
