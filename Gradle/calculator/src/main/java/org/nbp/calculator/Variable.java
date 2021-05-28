package org.nbp.calculator;

import org.nbp.common.LanguageUtilities;

public abstract class Variable {
  private final String variableName;

  protected Variable (String name) {
    variableName = name;
  }

  public final String getName () {
    return variableName;
  }

  public String getDescription () {
    return null;
  }

  public final <T extends AbstractNumber> T getValue (Class<T> type) {
    AbstractNumber value = getValue();
    if (value == null) return null;

    if (!LanguageUtilities.canAssign(type, value)) {
      try {
        value = newNumber(value.toString());
      } catch (NumberFormatException exception) {
        return null;
      }
    }

    return (T)value;
  }

  protected final AbstractNumber newNumber (String value) {
    return SavedSettings.getCalculatorMode().newNumber(value);
  }

  protected abstract AbstractNumber getValue ();
}
