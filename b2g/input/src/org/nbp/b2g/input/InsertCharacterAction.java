package org.nbp.b2g.input;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class InsertCharacterAction extends ScreenAction {
  private static final String LOG_TAG = InsertCharacterAction.class.getName();

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
    return true;
  }

  @Override
  public boolean performAction () {
    Character character = characterMap.get(Actions.getKeyMask());

    if (character != null) {
      InputService inputService = InputService.getInputService();

      if (inputService != null) {
        char value = character;
        ToggleAction control = ControlModifierAction.getControlModifier();

        if (control != null) {
          if (control.getState()) {
            value &= 0X1F;
          }
        }

        if (inputService.insertCharacter(value)) {
          return true;
        }
      }
    }

    return false;
  }

  public InsertCharacterAction () {
    super("INSERT_CHARACTER");
  }
}
