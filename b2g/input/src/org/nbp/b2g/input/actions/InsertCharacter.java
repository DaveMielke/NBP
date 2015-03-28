package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class InsertCharacter extends ScreenAction {
  private static final String LOG_TAG = InsertCharacter.class.getName();

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
    int keyMask = getKeyMask();
    Character character = characterMap.get(keyMask);

    if (character != null) {
      InputService inputService = InputService.getInputService();

      if (inputService != null) {
        char value = character;
        ModifierAction control = ControlModifier.getControlModifier();

        if (control != null) {
          if (control.getState()) {
            value &= 0X1F;
          }
        }

        if (inputService.insertCharacter(value)) {
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
