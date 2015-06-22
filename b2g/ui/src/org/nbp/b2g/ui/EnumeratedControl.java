package org.nbp.b2g.ui;

import android.content.SharedPreferences;

public abstract class EnumeratedControl<E extends Enum> extends IntegerControl {
  protected abstract E getEnumerationDefault ();
  protected abstract E getEnumerationValue ();
  protected abstract boolean setEnumerationValue (E value);

  private E[] getValues () {
    E value = getEnumerationDefault();

    E[] values = (E[])LanguageUtilities.invokeStaticMethod(
      value.getClass(), "values"
    );

    return values;
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
    if (value >= getValues().length) return false;
    return setEnumerationValue(getValues()[value]);
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
    return setEnumerationValue((E)(Enum.valueOf(getValues()[0].getClass(), prefs.getString(key, getEnumerationDefault().name()))));
  }

  protected EnumeratedControl (boolean isForDevelopers) {
    super(isForDevelopers);
  }
}
