package org.nbp.common;

import org.nbp.common.LanguageUtilities;

import android.content.SharedPreferences;

public abstract class EnumerationControl<E extends Enum> extends IntegerControl {
  protected abstract E getEnumerationDefault ();
  public abstract E getEnumerationValue ();
  protected abstract boolean setEnumerationValue (E value);

  private final Class getEnumerationClass () {
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

  private final int getValueCount () {
    return getValueArray().length;
  }

  private final E getValue (int ordinal) {
    return getValueArray()[ordinal];
  }

  private String[] labelArray = null;

  private final String[] getLabelArray () {
    synchronized (this) {
      if (labelArray == null) labelArray = new String[getValueCount()];
      return labelArray;
    }
  }

  protected String getValueLabel (E value) {
    String labels[] = getLabelArray();

    synchronized (labels) {
      int ordinal = value.ordinal();

      String label = labels[ordinal];
      if (label != null) return label;

      String resource = "enum_"
                      + value.getClass().getSimpleName()
                      + '_'
                      + value.name()
                      ;

      label = CommonContext.getString(resource);
      if (label == null) label = value.name().replace('_', ' ').toLowerCase();

      return labelArray[ordinal] = label;
    }
  }

  private final String getValueLabel (int ordinal) {
    return getValueLabel(getValue(ordinal));
  }

  public final String[] getValueLabels () {
    E[] values = getValueArray();
    int count = values.length;
    String[] labels = new String[count];

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
    reportValueChange();
    return true;
  }

  @Override
  public CharSequence getValue () {
    return getValueLabel(getEnumerationValue());
  }

  private String getLabel (int ordinal, int resource) {
    E[] values = getValueArray();
    if (values.length < 2) return null;
    if (values.length == 2) return getValueLabel(ordinal);
    return getString(resource);
  }

  @Override
  protected int getResourceForNext () {
    return R.string.control_next_default;
  }

  @Override
  public String getLabelForNext () {
    return getLabel(1, getResourceForNext());
  }

  @Override
  protected int getResourceForPrevious () {
    return R.string.control_previous_default;
  }

  @Override
  public String getLabelForPrevious () {
    return getLabel(0, getResourceForPrevious());
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putString(key, getEnumerationValue().name());
  }

  @Override
  protected boolean restoreValue (SharedPreferences prefs, String key) {
    E value = null;
    try {
      String name = prefs.getString(key, "");

      if (name.length() > 0) {
        try {
          value = (E)(Enum.valueOf(getEnumerationClass(), name));
        } catch (IllegalArgumentException exception) {
        }
      }
    } catch (ClassCastException exception) {
    }

    if (value == null) value = getEnumerationDefault();
    return setEnumerationValue(value);
  }

  protected EnumerationControl () {
    super();
  }
}
