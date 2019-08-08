package org.nbp.common.controls;
import org.nbp.common.*;

import org.nbp.common.LanguageUtilities;

import android.util.Log;
import android.content.SharedPreferences;

public abstract class EnumerationControl<E extends Enum> extends ItemControl {
  private final static String LOG_TAG = EnumerationControl.class.getName();

  protected abstract E getEnumerationDefault ();
  public abstract E getEnumerationValue ();
  protected abstract boolean setEnumerationValue (E value);

  private final Class<? extends Enum> getEnumerationClass () {
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

  @Override
  protected final int getValueCount () {
    return getValueArray().length;
  }

  protected String getValueLabel (E value) {
    String resource = "enum_"
                    + value.getClass().getSimpleName()
                    + '_'
                    + value.name()
                    ;

    String label = CommonContext.getString(resource);
    if (label == null) label = value.name().replace('_', ' ').toLowerCase();
    return label;
  }

  @Override
  protected final String getValueLabel (int index) {
    return getValueLabel(getValue(index));
  }

  private final E getValue (int index) {
    return getValueArray()[index];
  }

  protected boolean testEnumerationValue (E value) {
    return true;
  }

  @Override
  protected final boolean testIntegerValue (int value) {
    return testEnumerationValue(getValue(value));
  }

  @Override
  public final CharSequence[] getHighlightedItemLabels () {
    E[] values = getValueArray();
    int count = values.length;
    CharSequence[] labels = new CharSequence[count];

    for (int index=0; index<count; index+=1) {
      CharSequence label = getItemLabel(index);
      if (!testEnumerationValue(values[index])) label = highlightUnselectableLabel(label);
      labels[index] = label;
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
    synchronized (this) {
      if (value == getEnumerationValue()) return true;
      if (!testEnumerationValue(value)) return false;
      if (!setEnumerationValue(value)) return false;
      reportValueChange();
    }

    return true;
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putString(key, getEnumerationValue().name());
  }

  @Override
  protected ValueRestorer getValueRestorer () {
    return new ValueRestorer<E>() {
      @Override
      protected E getDefaultValue () {
        return getEnumerationDefault();
      }

      @Override
      protected E getSavedValue (SharedPreferences prefs, String key, E defaultValue) {
        String name = prefs.getString(key, "");

        if (name.length() > 0) {
          try {
            return (E)(Enum.valueOf(getEnumerationClass(), name));
          } catch (IllegalArgumentException exception) {
            Log.w(LOG_TAG,
              String.format(
                "saved value not recognized: %s: %s",
                getLabel(), name
              )
            );
          }
        }

        return defaultValue;
      }

      @Override
      protected boolean setCurrentValue (E value) {
        return setEnumerationValue(value);
      }

      @Override
      protected boolean testValue (E value) {
        return testEnumerationValue(value);
      }
    };
  }

  protected EnumerationControl () {
    super();
  }
}
