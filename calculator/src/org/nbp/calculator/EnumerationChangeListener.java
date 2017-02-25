package org.nbp.calculator;

import java.util.Map;
import java.util.HashMap;

import org.nbp.common.LanguageUtilities;

import android.view.View;
import android.widget.Button;

public class EnumerationChangeListener<E extends Enum> {
  public interface Handler {
    public void handleEnumerationChange (Enum value);
  }

  private final Class<E> enumerationType;
  private final Enum[] enumerationValues;
  private final Map<String, Enum> enumerationNames = new HashMap<String, Enum>();

  private final Button changeButton;
  private final Handler changeHandler;
  private final String settingName;
  private final Enum initialValue;

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
    SavedSettings.set(settingName, value);

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
    enumerationType = type;

    enumerationValues = (E[])LanguageUtilities.invokeStaticMethod(
      enumerationType, "values"
    );

    for (Enum value : enumerationValues) {
      enumerationNames.put(value.name(), value);
    }

    changeButton = button;
    changeHandler = handler;
    settingName = setting;
    initialValue = initial;

    changeSetting(SavedSettings.get(settingName, initialValue));
    registerCycleListener();
    registerResetListener();
  }
}
