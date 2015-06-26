package org.nbp.b2g.ui;

import android.content.SharedPreferences;

public abstract class EnumerationControl<E extends Enum> extends IntegerControl {
  protected abstract E getEnumerationDefault ();
  protected abstract E getEnumerationValue ();
  protected abstract boolean setEnumerationValue (E value);

  private Class getEnumerationClass () {
    return getEnumerationDefault().getClass();
  }

  private E[] valueArray = null;

  private E[] getValueArray () {
    synchronized (this) {
      if (valueArray == null) {
        valueArray = (E[])LanguageUtilities.invokeStaticMethod(
          getEnumerationClass(), "values"
        );
      }

      return valueArray;
    }
  }

  private int getValueCount () {
    return getValueArray().length;
  }

  private E getValue (int ordinal) {
    return getValueArray()[ordinal];
  }

  private String getValueLabel (E value) {
    return value.name().replace('_', ' ').toLowerCase();
  }

  private String getValueLabel (int ordinal) {
    return getValueLabel(getValue(ordinal));
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
    E[] values = getValueArray();
    if (value >= values.length) return false;
    return setEnumerationValue(values[value]);
  }

  @Override
  public String getValue () {
    return getValueLabel(getEnumerationValue());
  }

  private String getLabel (int ordinal, int resource) {
    E[] values = getValueArray();
    if (values.length < 2) return null;
    if (values.length == 2) return getValueLabel(ordinal);
    return ApplicationContext.getString(resource);
  }

  @Override
  public String getNextLabel () {
    return getLabel(1, R.string.default_control_next);
  }

  @Override
  public String getPreviousLabel () {
    return getLabel(0, R.string.default_control_previous);
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
