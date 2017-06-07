package org.nbp.common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;
import java.util.HashSet;

public abstract class Control {
  protected Control () {
  }

  protected abstract int getResourceForLabel ();

  public abstract String getValue ();
  protected abstract boolean setDefaultValue ();
  protected abstract boolean setNextValue ();
  protected abstract boolean setPreviousValue ();

  protected abstract void saveValue (SharedPreferences.Editor editor, String key);
  protected abstract boolean restoreValue (SharedPreferences prefs, String key);

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
    public abstract void confirm (String confirmation);
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
    public abstract void onValueChanged (Control control);
  }

  private final Set<OnValueChangedListener> onValueChangedListeners =
        new HashSet<OnValueChangedListener>();

  public final boolean addOnValueChangedListener (OnValueChangedListener listener) {
    return onValueChangedListeners.add(listener);
  }

  public final boolean removeOnValueChangedListener (OnValueChangedListener listener) {
    return onValueChangedListeners.remove(listener);
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

  private final boolean saveValue (SharedPreferences prefs) {
    String key = getPreferenceKey();
    if (key == null) return true;

    SharedPreferences.Editor editor = prefs.edit();
    saveValue(editor, key);
    return editor.commit();
  }

  protected final void reportValue (boolean confirm) {
    saveValue(getCurrentSettings());
    if (confirm) confirmValue();

    for (OnValueChangedListener listener : onValueChangedListeners) {
      listener.onValueChanged(this);
    }
  }

  public final boolean restoreDefaultValue () {
    if (!setDefaultValue()) return false;
    reportValue(false);
    return true;
  }

  public final static void restoreDefaultValues (Control[] controls) {
    for (Control control : controls) {
      control.restoreDefaultValue();
    }
  }

  private final boolean restoreValue (SharedPreferences prefs) {
    String key = getPreferenceKey();
    if (key == null) return restoreDefaultValue();

    if (!restoreValue(prefs, key)) return false;
    reportValue(false);
    return true;
  }

  public final boolean nextValue () {
    if (!setNextValue()) return false;
    reportValue(true);
    return true;
  }

  public final boolean previousValue () {
    if (!setPreviousValue()) return false;
    reportValue(true);
    return true;
  }

  public final boolean restoreCurrentValue () {
    return restoreValue(getCurrentSettings());
  }

  public final static void restoreCurrentValues (Control[] controls) {
    for (Control control : controls) {
      control.restoreCurrentValue();
    }
  }

  public final boolean saveValue () {
    return saveValue(getSavedSettings());
  }

  public final static void saveValues (Control[] controls) {
    for (Control control : controls) {
      control.saveValue();
    }
  }

  public final boolean restoreSavedValue () {
    return restoreValue(getSavedSettings());
  }

  public final static void restoreSavedValues (Control[] controls) {
    for (Control control : controls) {
      control.restoreSavedValue();
    }
  }
}
