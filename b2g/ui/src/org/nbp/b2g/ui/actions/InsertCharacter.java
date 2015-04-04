package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import java.lang.reflect.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class InsertCharacter extends ScreenAction {
  private final static String LOG_TAG = InsertCharacter.class.getName();

  public final static char CHAR_NUL   = 0X0000;
  public final static char CHAR_SOH   = 0X0001;
  public final static char CHAR_STX   = 0X0002;
  public final static char CHAR_ETX   = 0X0003;
  public final static char CHAR_EOT   = 0X0004;
  public final static char CHAR_ENQ   = 0X0005;
  public final static char CHAR_ACK   = 0X0006;
  public final static char CHAR_BEL   = 0X0007;
  public final static char CHAR_BS    = 0X0008;
  public final static char CHAR_HT    = 0X0009;
  public final static char CHAR_LF    = 0X000A;
  public final static char CHAR_VT    = 0X000B;
  public final static char CHAR_FF    = 0X000C;
  public final static char CHAR_CR    = 0X000D;
  public final static char CHAR_SO    = 0X000E;
  public final static char CHAR_SI    = 0X000F;
  public final static char CHAR_DLE   = 0X0010;
  public final static char CHAR_DC1   = 0X0011;
  public final static char CHAR_DC2   = 0X0012;
  public final static char CHAR_DC3   = 0X0013;
  public final static char CHAR_DC4   = 0X0014;
  public final static char CHAR_NAK   = 0X0015;
  public final static char CHAR_SYN   = 0X0016;
  public final static char CHAR_ETB   = 0X0017;
  public final static char CHAR_CAN   = 0X0018;
  public final static char CHAR_EM    = 0X0019;
  public final static char CHAR_SUB   = 0X001A;
  public final static char CHAR_ESC   = 0X001B;
  public final static char CHAR_FS    = 0X001C;
  public final static char CHAR_GS    = 0X001D;
  public final static char CHAR_RS    = 0X001E;
  public final static char CHAR_US    = 0X001F;
  public final static char CHAR_SPACE = 0X0020;
  public final static char CHAR_DEL   = 0X007F;

  protected Character getCharacter (String name) {
    name = "CHAR_" + name.toUpperCase();

    try {
      Field field = getClass().getField(name);
      int modifiers = field.getModifiers();

      if (Modifier.isStatic(modifiers)) {
        if (Modifier.isFinal(modifiers)) {
          Class type = field.getType();

          if (type.equals(char.class)) {
            return field.getChar(null);
          } else {
            Log.w(LOG_TAG, "field is not a char: " + name);
          }
        } else {
          Log.w(LOG_TAG, "field is not final: " + name);
        }
      } else {
        Log.w(LOG_TAG, "field is not static: " + name);
      }
    } catch (NoSuchFieldException exception) {
    } catch (IllegalAccessException exception) {
      Log.w(LOG_TAG, "field not accessible: " + name);
    }

    return null;
  }

  private static Map<Integer, Character> characterMap = new HashMap<Integer, Character>();

  @Override
  public boolean parseOperand (int keyMask, String operand) {
    char character;
    Character c = getCharacter(operand);

    if (c != null) {
      character = c;
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
    super(false);
  }
}
