package org.nbp.common.controls;
import org.nbp.common.*;

import android.content.SharedPreferences;

public abstract class FloatControl extends IntegerControl {
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

  protected int toIntegerValue (float floatValue) {
    return Math.round(toLinearValue(floatValue) * getLinearScale());
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
    return setFloatValue(toFloatValue((float)value / getLinearScale()));
  }

  public final boolean setValue (float value) {
    synchronized (this) {
      if (value == getFloatValue()) return true;
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
    return setFloatValue(prefs.getFloat(key, getFloatDefault()));
  }

  protected FloatControl () {
    super();
  }
}
