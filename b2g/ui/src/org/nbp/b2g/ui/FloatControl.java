package org.nbp.b2g.ui;

import android.content.SharedPreferences;

public abstract class FloatControl extends IntegerControl {
  protected abstract float getLinearScale ();
  protected abstract float getFloatDefault ();
  protected abstract float getFloatValue ();
  protected abstract boolean setFloatValue (float value);

  protected float toFloatValue (float linearValue) {
    return linearValue;
  }

  protected float toLinearValue (float floatValue) {
    return floatValue;
  }

  protected int toIntegerValue (float floatValue) {
    return Math.round(toLinearValue(floatValue) * getLinearScale());
  }

  @Override
  protected int getIntegerDefault () {
    return toIntegerValue(getFloatDefault());
  }

  @Override
  protected int getIntegerValue () {
    return toIntegerValue(getFloatValue());
  }

  @Override
  protected boolean setIntegerValue (int value) {
    return setFloatValue(toFloatValue((float)value / getLinearScale()));
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs) {
    return setFloatValue(prefs.getFloat(getPreferenceKey(), getFloatDefault()));
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor) {
    editor.putFloat(getPreferenceKey(), getFloatValue());
  }

  protected FloatControl () {
    super();
  }
}
