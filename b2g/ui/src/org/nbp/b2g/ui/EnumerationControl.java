package org.nbp.b2g.ui;

import android.content.SharedPreferences;

public abstract class EnumerationControl<E extends Enum> extends IntegerControl {
  protected abstract E getEnumerationDefault ();
  protected abstract E getEnumerationValue ();
  protected abstract boolean setEnumerationValue (E value);

  private Class getEnumerationClass () {
    return getEnumerationDefault().getClass();
  }

  private E[] enumerationValues = null;

  private E[] getEnumerationValues () {
    synchronized (this) {
      if (enumerationValues == null) {
        enumerationValues = (E[])LanguageUtilities.invokeStaticMethod(
          getEnumerationClass(), "values"
        );
      }

      return enumerationValues;
    }
  }

  @Override
  protected final int getIntegerDefault () {
    return getEnumerationDefault().ordinal();
  }

  @Override
  protected final int getIntegerValue () {
    return getEnumerationValue().ordinal();
  }

  @Override
  protected final boolean setIntegerValue (int value) {
    if (value < 0) return false;
    E[] values = getEnumerationValues();
    if (value >= values.length) return false;
    return setEnumerationValue(values[value]);
  }

  @Override
  public String getValue () {
    return getEnumerationValue().name().toLowerCase().replace('_', ' ');
  }

  @Override
  public String getNextLabel () {
    return ApplicationContext.getString(R.string.default_control_next);
  }

  @Override
  public String getPreviousLabel () {
    return ApplicationContext.getString(R.string.default_control_previous);
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putString(key, getEnumerationValue().name());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs, String key) {
    String name = prefs.getString(key, "");
    E value = (name.length() > 0)?
              (E)(Enum.valueOf(getEnumerationClass(), name)):
              null;

    if (value == null) value = getEnumerationDefault();
    return setEnumerationValue(value);
  }

  protected EnumerationControl (boolean isForDevelopers) {
    super(isForDevelopers);
  }
}
