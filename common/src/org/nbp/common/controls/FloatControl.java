package org.nbp.common.controls;
import org.nbp.common.*;

import android.util.Log;
import android.content.SharedPreferences;

public abstract class FloatControl extends IntegerControl {
  private final static String LOG_TAG = FloatControl.class.getName();

  protected abstract float getLinearScale ();
  protected abstract float getFloatDefault ();
  public abstract float getFloatValue ();
  protected abstract boolean setFloatValue (float value);

  protected float toFloatValue (float linearValue) {
    return linearValue;
  }

  protected float toLinearValue (float floatValue) {
    return floatValue;
  }

  protected float toFloatValue (int integerValue) {
    return toFloatValue((float)integerValue / getLinearScale());
  }

  protected int toIntegerValue (float floatValue) {
    return Math.round(toLinearValue(floatValue) * getLinearScale());
  }

  protected Float getFloatMinimum () {
    return null;
  }

  protected Float getFloatMaximum () {
    return null;
  }

  protected boolean testFloatValue (float value) {
    return true;
  }

  @Override
  protected final Integer getIntegerMinimum () {
    Float minimum = getFloatMinimum();
    if (minimum == null) return null;
    return toIntegerValue(minimum);
  }

  @Override
  protected final Integer getIntegerMaximum () {
    Float maximum = getFloatMaximum();
    if (maximum == null) return null;
    return toIntegerValue(maximum);
  }

  @Override
  protected final boolean testIntegerValue (int value) {
    return testFloatValue(toFloatValue(value));
  }

  @Override
  protected final int getIntegerDefault () {
    return toIntegerValue(getFloatDefault());
  }

  @Override
  public final int getIntegerValue () {
    return toIntegerValue(getFloatValue());
  }

  @Override
  protected final boolean setIntegerValue (int value) {
    return setFloatValue(toFloatValue(value));
  }

  public final boolean setValue (float value) {
    synchronized (this) {
      if (value == getFloatValue()) return true;
      if (!testFloatValue(value)) return false;
      if (!setFloatValue(value)) return false;
      reportValueChange();
    }

    return true;
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putFloat(key, getFloatValue());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs, String key) {
    float value = getFloatDefault();

    try {
      value = prefs.getFloat(key, value);
    } catch (ClassCastException exception) {
      Log.w(LOG_TAG, ("saved value not a float: " + getLabel()));
    }

    return setFloatValue(value);
  }

  protected FloatControl () {
    super();
  }
}
