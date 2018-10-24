package org.nbp.common.controls;
import org.nbp.common.*;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;

import java.util.List;
import java.util.ArrayList;

import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;

public abstract class Control {
  private final static String LOG_TAG = Control.class.getName();

  private final static Set<Control> allControls = new LinkedHashSet<Control>();
  private Set<Control> dependentControls = null;
  private final int orderNumber;

  protected Control () {
    synchronized (allControls) {
      orderNumber = allControls.size();
      allControls.add(this);
    }
  }

  public static Control[] getControlsInCreationOrder () {
    synchronized (allControls) {
      return allControls.toArray(new Control[allControls.size()]);
    }
  }

  public final void addDependencies (Control... controls) {
    synchronized (allControls) {
      if (dependentControls == null) {
        dependentControls = new LinkedHashSet<Control>();
      }

      for (Control control : controls) {
        dependentControls.add(control);
      }
    }
  }

  private static void addControls (Set<Control> controls, List<Control> list, boolean[] added) {
    for (Control control : controls) {
      int index = control.orderNumber;

      if (!added[index]) {
        if (control.dependentControls != null) {
          addControls(control.dependentControls, list, added);
        }

        if (added[index]) {
          Log.w(LOG_TAG, ("control dependency loop detected: " + control.getClass().getName()));
        } else {
          list.add(control);
          added[index] = true;
        }
      }
    }
  }

  public static Control[] getControlsInRestoreOrder () {
    synchronized (allControls) {
      int count = allControls.size();
      boolean[] added = new boolean[count];
      for (int index=0; index<count; index+=1) added[index] = false;

      List<Control> controls = new ArrayList<Control>();
      addControls(allControls, controls, added);

      return controls.toArray(new Control[count]);
    }
  }

  protected abstract int getResourceForLabel ();

  public abstract CharSequence getValue ();
  protected abstract boolean setDefaultValue ();
  protected abstract boolean setNextValue ();
  protected abstract boolean setPreviousValue ();

  protected abstract class ValueRestorer<T> {
    protected abstract T getDefaultValue ();
    protected abstract T getSavedValue (SharedPreferences prefs, String key, T defaultValue);
    protected abstract boolean setCurrentValue (T value);
    protected abstract boolean testValue (T value);

    public final boolean restoreValue (SharedPreferences prefs, String key) {
      T defaultValue = getDefaultValue();
      T savedValue;

      try {
        savedValue = getSavedValue(prefs, key, defaultValue);

        if (!testValue(savedValue)) {
          Log.w(LOG_TAG,
            String.format(
              "saved value not allowed: %s: %s",
              getLabel(), savedValue.toString()
            )
          );

          savedValue = defaultValue;
        }
      } catch (ClassCastException exception) {
        Log.w(LOG_TAG,
          String.format(
            "incorrect saved value type: %s: %s",
            getLabel(), exception.getMessage()
          )
        );

        savedValue = defaultValue;
      }

      return setCurrentValue(savedValue);
    }
  }

  protected abstract void saveValue (SharedPreferences.Editor editor, String key);
  protected abstract ValueRestorer getValueRestorer ();

  protected final boolean restoreValue (SharedPreferences prefs, String key) {
    return getValueRestorer().restoreValue(prefs, key);
  }

  protected final static String getString (int resource) {
    return CommonContext.getString(resource);
  }

  public final String getLabel () {
    return getString(getResourceForLabel());
  }

  protected int getResourceForGroup () {
    return 0;
  }

  public final String getGroup () {
    int resource = getResourceForGroup();
    if (resource == 0) resource = R.string.control_group_main;
    return getString(resource);
  }

  protected int getResourceForNext () {
    return R.string.control_next_default;
  }

  public String getLabelForNext () {
    return getString(getResourceForNext());
  }

  protected int getResourceForPrevious () {
    return R.string.control_previous_default;
  }

  public String getLabelForPrevious () {
    return getString(getResourceForPrevious());
  }

  public interface ConfirmationListener {
    public void confirm (String confirmation);
  }

  private static ConfirmationListener confirmationListener = null;

  public final static ConfirmationListener getConfirmationListener () {
    return confirmationListener;
  }

  public final static void setConfirmationListener (ConfirmationListener listener) {
    confirmationListener = listener;
  }

  public final static void confirm (String confirmation) {
    if (confirmationListener != null) {
      confirmationListener.confirm(confirmation);
    }
  }

  public final static void confirm (int confirmation) {
    confirm(getString(confirmation));
  }

  protected String getValueConfirmation () {
    return getLabel() + " " + getValue();
  }

  public final void confirmValue () {
    confirm(getValueConfirmation());
  }

  public interface OnValueChangedListener {
    public void onValueChanged (Control control);
  }

  private final Set<OnValueChangedListener> onValueChangedListeners =
        new HashSet<OnValueChangedListener>();

  public final boolean addOnValueChangedListener (OnValueChangedListener listener) {
    return onValueChangedListeners.add(listener);
  }

  public final boolean removeOnValueChangedListener (OnValueChangedListener listener) {
    return onValueChangedListeners.remove(listener);
  }

  protected final void callOnValueChangedListeners () {
    for (OnValueChangedListener listener : onValueChangedListeners) {
      listener.onValueChanged(this);
    }
  }

  private final static SharedPreferences getSettings (String name) {
    return CommonContext.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
  }

  private final static SharedPreferences getCurrentSettings () {
    return getSettings("current-settings");
  }

  private final static SharedPreferences getSavedSettings () {
    return getSettings("saved-settings");
  }

  protected String getPreferenceKey () {
    return null;
  }

  private final void saveValue (SharedPreferences prefs) {
    String key = getPreferenceKey();

    if (key != null) {
      SharedPreferences.Editor editor = prefs.edit();
      saveValue(editor, key);
      editor.apply();
    }
  }

  protected final void reportValueChange () {
    saveValue(getCurrentSettings());
    callOnValueChangedListeners();
  }

  private final void confirmValueChange () {
    confirmValue();
    reportValueChange();
  }

  public final boolean restoreDefaultValue () {
    if (!setDefaultValue()) return false;
    reportValueChange();
    return true;
  }

  public final static void restoreDefaultValues (Control... controls) {
    for (Control control : controls) {
      control.restoreDefaultValue();
    }
  }

  private final boolean restoreValue (SharedPreferences prefs) {
    String key = getPreferenceKey();
    if (key == null) return restoreDefaultValue();

    if (!restoreValue(prefs, key)) return false;
    reportValueChange();
    return true;
  }

  public final boolean nextValue () {
    if (!setNextValue()) return false;
    confirmValueChange();
    return true;
  }

  public final boolean previousValue () {
    if (!setPreviousValue()) return false;
    confirmValueChange();
    return true;
  }

  public final boolean restoreCurrentValue () {
    return restoreValue(getCurrentSettings());
  }

  public final static void restoreCurrentValues (Control... controls) {
    for (Control control : controls) {
      control.restoreCurrentValue();
    }
  }

  public final void saveValue () {
    saveValue(getSavedSettings());
  }

  public final static void saveValues (Control... controls) {
    for (Control control : controls) {
      control.saveValue();
    }
  }

  public final boolean restoreSavedValue () {
    return restoreValue(getSavedSettings());
  }

  public final static void restoreSavedValues (Control... controls) {
    for (Control control : controls) {
      control.restoreSavedValue();
    }
  }
}
