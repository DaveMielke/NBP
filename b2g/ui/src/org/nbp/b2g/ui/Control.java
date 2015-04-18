package org.nbp.b2g.ui;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;
import java.util.HashSet;

public abstract class Control {
  public abstract boolean setNextValue ();
  public abstract boolean setPreviousValue ();
  public abstract boolean setDefaultValue ();

  public abstract String getLabel ();
  public abstract String getValue ();

  protected abstract void saveValue (SharedPreferences.Editor editor, String key);
  protected abstract boolean restoreValue (SharedPreferences prefs, String key);

  protected String getPreferenceKey () {
    return null;
  }

  private static SharedPreferences getSharedPreferences () {
    return ApplicationContext.getContext().getSharedPreferences("controls", Context.MODE_PRIVATE);
  }

  public boolean saveValue () {
    String key = getPreferenceKey();
    if (key == null) return true;

    SharedPreferences.Editor editor = getSharedPreferences().edit();
    saveValue(editor, key);
    return editor.commit();
  }

  public boolean restoreValue () {
    String key = getPreferenceKey();
    if (key == null) return resetValue();
    return restoreValue(getSharedPreferences(), key);
  }

  public boolean resetValue () {
    return setDefaultValue();
  }

  public abstract static class OnValueChangeListener {
    public abstract void onValueChange (Control control);
  }

  private Set<OnValueChangeListener> onValueChangeListeners = new HashSet<OnValueChangeListener>();

  public boolean addOnValueChangeListener (OnValueChangeListener listener) {
    return onValueChangeListeners.add(listener);
  }

  public boolean removeOnValueChangeListener (OnValueChangeListener listener) {
    return onValueChangeListeners.remove(listener);
  }

  protected void reportValue () {
    ApplicationUtilities.message(getLabel() + " " + getValue());

    for (OnValueChangeListener listener : onValueChangeListeners) {
      listener.onValueChange(this);
    }
  }

  public Control () {
  }
}
