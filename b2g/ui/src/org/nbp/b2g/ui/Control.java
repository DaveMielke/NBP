package org.nbp.b2g.ui;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;
import java.util.HashSet;

import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public abstract class Control {
  protected abstract boolean setNextValue ();
  protected abstract boolean setPreviousValue ();
  protected abstract boolean setDefaultValue ();

  public abstract CharSequence getLabel ();
  public abstract CharSequence getValue ();

  protected abstract void saveValue (SharedPreferences.Editor editor, String key);
  protected abstract boolean restoreValue (SharedPreferences prefs, String key);

  public CharSequence getNextLabel () {
    return ApplicationContext.getString(R.string.default_control_next);
  }

  public CharSequence getPreviousLabel () {
    return ApplicationContext.getString(R.string.default_control_previous);
  }

  private final static StyleSpan HEADER_SPAN = new StyleSpan(Typeface.BOLD);

  protected final CharSequence toHeader (CharSequence text) {
    SpannableStringBuilder header = new SpannableStringBuilder(text);
    header.setSpan(HEADER_SPAN, 0, text.length(), 0);
    return header;
  }

  protected String getPreferenceKey () {
    return null;
  }

  private final static SharedPreferences getSettings (String name) {
    return ApplicationContext.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
  }

  private final boolean saveValue (SharedPreferences prefs) {
    String key = getPreferenceKey();
    if (key == null) return true;

    SharedPreferences.Editor editor = prefs.edit();
    saveValue(editor, key);
    return editor.commit();
  }

  public interface OnValueChangedListener {
    public abstract void onValueChanged (Control control);
  }

  private final Set<OnValueChangedListener> onValueChangedListeners = new HashSet<OnValueChangedListener>();

  public final boolean addOnValueChangedListener (OnValueChangedListener listener) {
    return onValueChangedListeners.add(listener);
  }

  public final boolean removeOnValueChangedListener (OnValueChangedListener listener) {
    return onValueChangedListeners.remove(listener);
  }

  private final static SharedPreferences getCurrentSettings () {
    return getSettings("current-settings");
  }

  protected String getConfirmation () {
    return getLabel() + " " + getValue();
  }

  public final void confirmValue () {
    ApplicationUtilities.message(getConfirmation());
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

  private final boolean restoreValue (SharedPreferences prefs) {
    String key = getPreferenceKey();
    if (key == null) return restoreDefaultValue();

    if (!restoreValue(prefs, key)) return false;
    reportValue(false);
    return true;
  }

  public final boolean restoreCurrentValue () {
    return restoreValue(getCurrentSettings());
  }

  private final static SharedPreferences getSavedSettings () {
    return getSettings("saved-settings");
  }

  public final boolean saveValue () {
    return saveValue(getSavedSettings());
  }

  public final boolean restoreSavedValue () {
    return restoreValue(getSavedSettings());
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

  private final boolean isDeveloperControl;

  public final boolean isForDevelopers () {
    return isDeveloperControl;
  }

  public Control (boolean isForDevelopers) {
    isDeveloperControl = isForDevelopers;
  }
}
