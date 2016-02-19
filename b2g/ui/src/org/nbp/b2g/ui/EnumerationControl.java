package org.nbp.b2g.ui;

import org.nbp.common.LanguageUtilities;

import android.content.SharedPreferences;

public abstract class EnumerationControl<E extends Enum> extends IntegerControl {
  protected abstract E getEnumerationDefault ();
  public abstract E getEnumerationValue ();
  protected abstract boolean setEnumerationValue (E value);

  private Class getEnumerationClass () {
    return getEnumerationDefault().getClass();
  }

  private E[] valueArray = null;

  private final E[] getValueArray () {
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

  protected CharSequence getValueLabel (E value) {
    String label = EnumerationLabels.getLabel(value);
    if (label != null) return label;
    return value.name().replace('_', ' ').toLowerCase();
  }

  private CharSequence getValueLabel (int ordinal) {
    return getValueLabel(getValue(ordinal));
  }

  public final CharSequence[] getValueLabels () {
    E[] values = getValueArray();
    int count = values.length;
    CharSequence[] labels = new CharSequence[count];

    for (int index=0; index<count; index+=1) {
      labels[index] = getValueLabel(values[index]);
    }

    return labels;
  }

  @Override
  protected final int getIntegerDefault () {
    return getEnumerationDefault().ordinal();
  }

  @Override
  public final int getIntegerValue () {
    return getEnumerationValue().ordinal();
  }

  @Override
  protected final boolean setIntegerValue (int value) {
    if (value < 0) return false;
    E[] values = getValueArray();
    if (value >= values.length) return false;
    return setEnumerationValue(values[value]);
  }

  public final boolean setValue (E value) {
    if (!setEnumerationValue(value)) return false;
    reportValue(false);
    return true;
  }

  @Override
  public CharSequence getValue () {
    return getValueLabel(getEnumerationValue());
  }

  private CharSequence getLabel (int ordinal, int resource) {
    E[] values = getValueArray();
    if (values.length < 2) return null;
    if (values.length == 2) return getValueLabel(ordinal);
    return getString(resource);
  }

  @Override
  public CharSequence getNextLabel () {
    return getLabel(1, R.string.default_control_next);
  }

  @Override
  public CharSequence getPreviousLabel () {
    return getLabel(0, R.string.default_control_previous);
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putString(key, getEnumerationValue().name());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs, String key) {
    E value = null;
    String name = prefs.getString(key, "");

    if (name.length() > 0) {
      try {
        value = (E)(Enum.valueOf(getEnumerationClass(), name));
      } catch (IllegalArgumentException exception) {
      }
    }

    if (value == null) value = getEnumerationDefault();
    return setEnumerationValue(value);
  }

  protected EnumerationControl (boolean isForDevelopers) {
    super(isForDevelopers);
  }
}
