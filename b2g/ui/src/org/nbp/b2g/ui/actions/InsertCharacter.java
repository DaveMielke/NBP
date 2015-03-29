package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class InsertCharacter extends ScreenAction {
  private final static String LOG_TAG = InsertCharacter.class.getName();

  private static Map<Integer, Character> characterMap = new HashMap<Integer, Character>();

  private boolean setBrailleCharacter (char character, int keyMask) {
    if (keyMask == 0) return false;
    if (keyMask == KeyMask.SPACE) keyMask = 0;
    if ((keyMask & ~KeyMask.DOTS_12345678) != 0) return false;

    byte dots = 0;
    if ((keyMask & KeyMask.DOT_1) != 0) dots |= BrailleDevice.DOT_1;
    if ((keyMask & KeyMask.DOT_2) != 0) dots |= BrailleDevice.DOT_2;
    if ((keyMask & KeyMask.DOT_3) != 0) dots |= BrailleDevice.DOT_3;
    if ((keyMask & KeyMask.DOT_4) != 0) dots |= BrailleDevice.DOT_4;
    if ((keyMask & KeyMask.DOT_5) != 0) dots |= BrailleDevice.DOT_5;
    if ((keyMask & KeyMask.DOT_6) != 0) dots |= BrailleDevice.DOT_6;
    if ((keyMask & KeyMask.DOT_7) != 0) dots |= BrailleDevice.DOT_7;
    if ((keyMask & KeyMask.DOT_8) != 0) dots |= BrailleDevice.DOT_8;

    return BrailleDevice.setCharacter(character, dots);
  }

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
    setBrailleCharacter(character, keyMask);
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
