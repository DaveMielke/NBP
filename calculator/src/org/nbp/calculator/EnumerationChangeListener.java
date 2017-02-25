package org.nbp.calculator;

import java.util.Map;
import java.util.HashMap;

import org.nbp.common.LanguageUtilities;

import android.view.View;
import android.widget.Button;

public class EnumerationChangeListener<E extends Enum<E>> {
  public interface Handler {
    public void handleEnumerationChange (Enum value);
  }

  private final Class<E> enumerationType;
  private final E[] enumerationValues;
  private final Map<String, Enum> enumerationNames = new HashMap<String, Enum>();

  private final Button changeButton;
  private final Handler changeHandler;
  private final String settingName;
  private final E initialValue;

  private final String getLabel (int index) {
    return (String)LanguageUtilities.invokeInstanceMethod(
      enumerationValues[index], "getLabel"
    );
  }

  private final String getDescription (int index) {
    return (String)LanguageUtilities.invokeInstanceMethod(
      enumerationValues[index], "getDescription"
    );
  }

  private final void changeSetting (int index) {
    Enum value = enumerationValues[index];
    changeButton.setHint(value.name());
    changeButton.setText(getLabel(index));
    changeButton.setContentDescription(getDescription(index));

    if (settingName != null) {
      SavedSettings.set(settingName, value);
    }

    if (changeHandler != null) {
      changeHandler.handleEnumerationChange(value);
    }
  }

  private final void changeSetting (Enum value) {
    changeSetting(value.ordinal());
  }

  private final void changeSetting (String name) {
    changeSetting(enumerationNames.get(name));
  }

  private final void registerCycleListener () {
    changeButton.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          String name = changeButton.getHint().toString();
          int index = enumerationNames.get(name).ordinal();
          if ((index += 1) == enumerationValues.length) index = 0;
          changeSetting(index);
        }
      }
    );
  }

  private final void registerResetListener () {
    changeButton.setOnLongClickListener(
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          changeSetting(initialValue);
          return true;
        }
      }
    );
  }

  public EnumerationChangeListener (
    Button button, Class<E> type, String setting, E initial, Handler handler
  ) {
    changeButton = button;
    changeHandler = handler;
    enumerationType = type;
    settingName = setting;

    enumerationValues = (E[])LanguageUtilities.invokeStaticMethod(
      enumerationType, "values"
    );

    for (Enum value : enumerationValues) {
      enumerationNames.put(value.name(), value);
    }

    if (initial == null) initial = enumerationValues[0];
    initialValue = initial;

    if (settingName != null) {
      changeSetting(SavedSettings.get(settingName, type, initialValue));
    }

    registerCycleListener();
    registerResetListener();
  }

  public EnumerationChangeListener (
    Button button, Class<E> type, String setting, E initial
  ) {
    this(button, type, setting, initial, null);
  }

  public EnumerationChangeListener (
    Button button, Class<E> type, String setting
  ) {
    this(button, type, setting, null);
  }

  public EnumerationChangeListener (Button button, Class<E> type) {
    this(button, type, null);
  }
}
