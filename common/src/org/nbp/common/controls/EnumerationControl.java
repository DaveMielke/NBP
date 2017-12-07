package org.nbp.common.controls;
import org.nbp.common.*;

import org.nbp.common.LanguageUtilities;

import android.util.Log;
import android.content.SharedPreferences;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;

public abstract class EnumerationControl<E extends Enum> extends IntegerControl {
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

  private final int getValueCount () {
    return getValueArray().length;
  }

  private final static Integer minimumValue = 0;
  private static Integer maximumValue = null;

  @Override
  protected Integer getIntegerMinimum () {
    return minimumValue;
  }

  @Override
  protected Integer getIntegerMaximum () {
    synchronized (minimumValue) {
      if (maximumValue == null) maximumValue = getValueCount() - 1;
    }

    return maximumValue;
  }

  private final E getValue (int ordinal) {
    return getValueArray()[ordinal];
  }

  protected boolean testEnumerationValue (E value) {
    return true;
  }

  @Override
  protected final boolean testIntegerValue (int value) {
    return testEnumerationValue(getValue(value));
  }

  private String[] labelArray = null;

  private final String[] getLabelArray () {
    synchronized (this) {
      if (labelArray == null) labelArray = new String[getValueCount()];
      return labelArray;
    }
  }

  public String getValueLabel (E value) {
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

  public final CharSequence[] newValueLabels () {
    E[] values = getValueArray();
    int count = values.length;
    CharSequence[] labels = new CharSequence[count];

    for (int ordinal=0; ordinal<count; ordinal+=1) {
      E value = values[ordinal];
      CharSequence label = getValueLabel(value);

      if (!testEnumerationValue(value)) {
        Spannable text = new SpannableString(label);
        text.setSpan(new StrikethroughSpan(), 0, text.length(), text.SPAN_EXCLUSIVE_EXCLUSIVE);
        label = text;
      }

      labels[ordinal] = label;
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
    String name = prefs.getString(key, "");

    if (name.length() > 0) {
      try {
        value = (E)(Enum.valueOf(getEnumerationClass(), name));

        if (!testEnumerationValue(value)) {
          value = null;

          Log.w(LOG_TAG,
            String.format(
              "control setting not available: %s: %s",
              getLabel(), name
            )
          );
        }
      } catch (IllegalArgumentException exception) {
        Log.w(LOG_TAG,
          String.format(
            "unrecognized control setting: %s: %s",
            getLabel(), name
          )
        );
      } catch (ClassCastException exception) {
        Log.w(LOG_TAG,
          String.format(
            "invalid control setting type: %s: %s",
            getLabel(), name
          )
        );
      }
    }

    if (value == null) value = getEnumerationDefault();
    return setEnumerationValue(value);
  }

  protected EnumerationControl () {
    super();
  }
}
