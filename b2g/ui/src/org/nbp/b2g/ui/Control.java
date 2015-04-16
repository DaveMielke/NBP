package org.nbp.b2g.ui;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class Control {
  public abstract boolean setNextValue ();
  public abstract boolean setPreviousValue ();
  public abstract boolean setDefaultValue ();

  public abstract String getValue ();
  protected abstract String getLabel ();

  protected abstract boolean restoreValue (SharedPreferences prefs, String key);
  protected abstract void saveValue (SharedPreferences.Editor editor, String key);

  protected String getPreferenceKey () {
    return null;
  }

  protected void reportValue () {
    ApplicationUtilities.message(getLabel() + " " + getValue());
  }

  private static SharedPreferences getSharedPreferences () {
    return ApplicationContext.get().getSharedPreferences("controls", Context.MODE_PRIVATE);
  }

  public boolean restoreValue () {
    String key = getPreferenceKey();
    if (key == null) return setDefaultValue();
    return restoreValue(getSharedPreferences(), key);
  }

  public boolean saveValue () {
    String key = getPreferenceKey();
    if (key == null) return true;

    SharedPreferences.Editor editor = getSharedPreferences().edit();
    saveValue(editor, key);
    return editor.commit();
  }

  public Control () {
  }
}
