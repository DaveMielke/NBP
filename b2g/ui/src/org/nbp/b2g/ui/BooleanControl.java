package org.nbp.b2g.ui;

import android.content.SharedPreferences;

public abstract class BooleanControl extends Control {
  public abstract boolean getBooleanValue ();
  protected abstract boolean setBooleanValue (boolean value);

  protected boolean getBooleanDefault () {
    return false;
  }

  private final boolean changeValue (boolean value) {
    if (value == getBooleanValue()) return false;
    return setBooleanValue(value);
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
    if (!setBooleanValue(value)) return false;
    reportValue(false);
    return true;
  }

  @Override
  public CharSequence getValue () {
    return getBooleanValue()? getNextLabel(): getPreviousLabel();
  }

  @Override
  public CharSequence getNextLabel () {
    return getString(R.string.boolean_control_next);
  }

  @Override
  public CharSequence getPreviousLabel () {
    return getString(R.string.boolean_control_previous);
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putBoolean(key, getBooleanValue());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs, String key) {
    return setBooleanValue(prefs.getBoolean(key, getBooleanDefault()));
  }

  protected BooleanControl (boolean isForDevelopers) {
    super(isForDevelopers);
  }
}
