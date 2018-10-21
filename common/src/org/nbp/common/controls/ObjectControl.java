package org.nbp.common.controls;
import org.nbp.common.*;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;
import android.content.SharedPreferences;

public abstract class ObjectControl<T> extends ChoiceControl {
  private final static String LOG_TAG = ObjectControl.class.getName();

  protected abstract T getObjectDefault ();
  protected abstract T getObjectValue ();
  protected abstract boolean setObjectValue (T value);

  protected abstract String getValueName (T value);
  protected abstract String getValueLabel (T value);

  private final ArrayList<T> valueList = new ArrayList<T>();
  private final Map<String, Integer> valueIndex = new HashMap<String, Integer>();

  protected final void addObjectValue (T value) {
    synchronized (this) {
      int index = valueList.size();
      valueList.add(value);
      valueIndex.put(getValueName(value), index);
    }
  }

  public final T getValue (int index) {
    return valueList.get(index);
  }

  public final T getValue (String key) {
    Integer index = valueIndex.get(key);
    if (index == null) return null;
    return getValue(index);
  }

  @Override
  protected final int getValueCount () {
    return valueList.size();
  }

  @Override
  protected final String getValueLabel (int index) {
    return getValueLabel(getValue(index));
  }

  protected boolean testObjectValue (T value) {
    return true;
  }

  @Override
  protected final boolean testIntegerValue (int value) {
    return testObjectValue(getValue(value));
  }

  @Override
  public final CharSequence[] getHighlightedLabels () {
    int count = valueList.size();
    CharSequence[] labels = new CharSequence[count];

    for (int index=0; index<count; index+=1) {
      T value = valueList.get(index);
      CharSequence label = getValueLabel(value);
      if (!testObjectValue(value)) label = highlightUnselectableLabel(label);
      labels[index] = label;
    }

    return labels;
  }

  private final int getIntegerValue (T value) {
    Integer index = valueIndex.get(getValueName(value));
    if (index == null) return 0;
    return index;
  }

  @Override
  protected final int getIntegerDefault () {
    return getIntegerValue(getObjectDefault());
  }

  @Override
  public final int getIntegerValue () {
    return getIntegerValue(getObjectValue());
  }

  @Override
  protected final boolean setIntegerValue (int value) {
    if (value < 0) return false;
    if (value >= valueList.size()) return false;
    return setObjectValue(valueList.get(value));
  }

  public final boolean setValue (T value) {
    synchronized (this) {
      if (value == getObjectValue()) return true;
      if (!testObjectValue(value)) return false;
      if (!setObjectValue(value)) return false;
      reportValueChange();
    }

    return true;
  }

  @Override
  public CharSequence getValue () {
    return getValueLabel(getObjectValue());
  }

  @Override
  protected void saveValue (SharedPreferences.Editor editor, String key) {
    editor.putString(key, getValueName(getObjectValue()));
  }

  @Override
  protected ValueRestorer getValueRestorer () {
    return new ValueRestorer<T>() {
      @Override
      protected T getDefaultValue () {
        return getObjectDefault();
      }

      @Override
      protected T getSavedValue (SharedPreferences prefs, String key, T defaultValue) {
        String name = prefs.getString(key, "");
        if (name.isEmpty()) return defaultValue;

        Integer index = valueIndex.get(name);
        if (index == null) return defaultValue;

        return valueList.get(index);
      }

      @Override
      protected boolean setCurrentValue (T value) {
        return setObjectValue(value);
      }

      @Override
      protected boolean testValue (T value) {
        return testObjectValue(value);
      }
    };
  }

  protected ObjectControl () {
    super();
  }
}
