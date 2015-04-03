package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class InsertCharacter extends ScreenAction {
  private final static String LOG_TAG = InsertCharacter.class.getName();

  private static Map<Integer, Character> characterMap = new HashMap<Integer, Character>();

  @Override
  public boolean parseOperand (int keyMask, String operand) {
    char character;

    if (operand.equals("space")) {
      character = ' ';
    } else if (operand.length() == 1) {
      character = operand.charAt(0);
    } else {
      return super.parseOperand(keyMask, operand);
    }

    characterMap.put(keyMask, character);
    BrailleDevice.setCharacter(character, keyMask);
    return true;
  }

  @Override
  public boolean performAction () {
    ModifierAction control = ControlModifier.getControlModifier();
    int keyMask = getNavigationKeys();
    Character character = characterMap.get(keyMask);

    if (character != null) {
      InputService inputService = InputService.getInputService();

      if (inputService != null) {
        char value = character;

        if (control != null) {
          if (control.getState()) {
            if ((value >= 0X40) && (value <= 0X7E)) {
              value &= 0X1F;
            } else if (value == 0X3F) {
              value |= 0X40;
            } else {
              ApplicationUtilities.beep();
              return false;
            }
          }
        }

        if (inputService.insert(value)) {
          return true;
        }
      }
    } else {
      Log.w(LOG_TAG, String.format("not mapped to a character: 0X%02X", keyMask));
    }

    return false;
  }

  public InsertCharacter () {
    super();
  }
}
