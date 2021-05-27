package org.nbp.common.controls;
import org.nbp.common.*;

import android.content.SharedPreferences;

public abstract class BooleanControl extends Control {
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
  protected ValueRestorer getValueRestorer () {
    return new ValueRestorer<Boolean>() {
      @Override
      protected Boolean getDefaultValue () {
        return getBooleanDefault();
      }

      @Override
      protected Boolean getSavedValue (SharedPreferences prefs, String key, Boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
      }

      @Override
      protected boolean setCurrentValue (Boolean value) {
        return setBooleanValue(value);
      }

      @Override
      protected boolean testValue (Boolean value) {
        return testBooleanValue(value);
      }
    };
  }

  protected BooleanControl () {
    super();
  }
}
