package org.nbp.b2g.ui;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import java.util.Locale;

import android.util.Log;

public class Characters {
  private final static String LOG_TAG = Characters.class.getName();

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

  private final static CharacterFieldMap characterFields = new CharacterFieldMap() {
    @Override
    protected final String getMapType () {
      return "character";
    }

    @Override
    protected final String getNamePrefix () {
      return "CHAR_";
    }
  };

  static {
    Log.d(LOG_TAG, "begin mapping character fields");

    CharacterFieldMap.makeMaps(
      Characters.class,
      characterFields
    );

    Log.d(LOG_TAG, "end mapping character fields");
  }

  public static Character getCharacter (String name) {
    return characterFields.getValue(name.toUpperCase());
  }

  private final Map<Integer, Character> characterMap = new HashMap<Integer, Character>();

  private final void setCharacter (Integer keyMask, Character character) {
    characterMap.put(keyMask, character);
  }

  private final Character getCharacter (Integer keyMask) {
    return characterMap.get(keyMask);
  }

  public final Character toCharacter (int keyMask) {
    return getCharacter(keyMask);
  }

  private final Map<Character, Byte> dotsMap = new HashMap<Character, Byte>();

  private final void setDots (Character character, Byte dots) {
    dotsMap.put(character, dots);
  }

  private final Byte getDots (Character character) {
    return dotsMap.get(character);
  }

  public final Byte toDots (char character) {
    {
      Byte dots = getDots(character);
      if (dots != null) return dots;
    }

    {
      Character.UnicodeBlock block = Character.UnicodeBlock.of(character);

      if (block.equals(Character.UnicodeBlock.BRAILLE_PATTERNS)) {
        byte dots = 0;

        if ((character & Braille.UNICODE_DOT_1) != 0) dots |= BrailleDevice.DOT_1;
        if ((character & Braille.UNICODE_DOT_2) != 0) dots |= BrailleDevice.DOT_2;
        if ((character & Braille.UNICODE_DOT_3) != 0) dots |= BrailleDevice.DOT_3;
        if ((character & Braille.UNICODE_DOT_4) != 0) dots |= BrailleDevice.DOT_4;
        if ((character & Braille.UNICODE_DOT_5) != 0) dots |= BrailleDevice.DOT_5;
        if ((character & Braille.UNICODE_DOT_6) != 0) dots |= BrailleDevice.DOT_6;
        if ((character & Braille.UNICODE_DOT_7) != 0) dots |= BrailleDevice.DOT_7;
        if ((character & Braille.UNICODE_DOT_8) != 0) dots |= BrailleDevice.DOT_8;

        setDots(character, dots);
        return dots;
      }
    }

    {
      char base = UnicodeUtilities.getBaseCharacter(character);

      if (base != character) {
        Byte dots = getDots(base);

        if (dots != null) {
          setDots(character, dots);
          return dots;
        }
      }
    }

    return null;
  }

  private static Character parseCharacter (String operand) {
    {
      Character character = getCharacter(operand);
      if (character != null) return character;
    }

    final int length = operand.length();
    if (length == 1) return operand.charAt(0);

    if ((length == 6) && operand.startsWith("U+")) {
      String digits = operand.substring(2);
      int radix = 16;

      if (Character.digit(digits.charAt(0), radix) >= 0) {
        try {
          int value = Integer.parseInt(digits, radix);

          if ((value >= Character.MIN_VALUE) && (value <= Character.MAX_VALUE)) {
            return (char)value;
          }
        } catch (NumberFormatException exception) {
        }
      }
    }

    Log.w(LOG_TAG, "unknown character: " + operand);
    return null;
  }

  private static Integer parseKeys (String operand) {
    int length = operand.length();
    int mask = 0;

    for (int index=0; index<length; index+=1) {
      char character = operand.charAt(index);
      Integer bit = KeyMask.toBit(Character.toUpperCase(character));

      if (bit == null) {
        Log.w(LOG_TAG, "unknown key: " + character);
        return null;
      }

      if ((mask & bit) != 0) {
        Log.w(LOG_TAG, "key specified more than once: " + operand);
        return null;
      }

      mask |= bit;
    }

    return mask;
  }

  private final boolean defineCharacter (String[] operands, boolean forInput) {
    int index = 0;

    if (index == operands.length) {
      Log.w(LOG_TAG, "character not specified");
      return true;
    }

    String characterOperand = operands[index++];
    Character character = parseCharacter(characterOperand);
    if (character == null) return true;

    if (index == operands.length) {
      Log.w(LOG_TAG, "keys not specified");
      return true;
    }

    String keysOperand = operands[index++];
    Integer keyMask = parseKeys(keysOperand);
    if (keyMask == null) return true;
    Byte dots = KeyMask.toDots(keyMask);

    if (dots == null) {
      Log.w(LOG_TAG, "not space or just dots: " + keysOperand);
      return true;
    }

    if (index < operands.length) {
      Log.w(LOG_TAG, "too many operands");
    }

    if (forInput) {
      Character old = getCharacter(keyMask);

      if (old != null) {
        if (!old.equals(character)) {
          Log.w(LOG_TAG, "key combination already bound: " + keysOperand);
          return true;
        }

        forInput = false;
      }
    }

    {
      Byte old = getDots(character);

      if (old == null) {
        setDots(character, dots);
      } else if (!old.equals(dots)) {
        Log.w(LOG_TAG, "character already defined: " + characterOperand);
        return true;
      }
    }

    if (forInput) setCharacter(keyMask, character);
    return true;
  }

  private final InputProcessor makeInputProcessor () {
    DirectiveProcessor directiveProcessor = new DirectiveProcessor();

    directiveProcessor.addDirective("char", new DirectiveProcessor.DirectiveHandler() {
      @Override
      public boolean handleDirective (String[] operands) {
        return defineCharacter(operands, true);
      }
    });

    directiveProcessor.addDirective("glyph", new DirectiveProcessor.DirectiveHandler() {
      @Override
      public boolean handleDirective (String[] operands) {
        return defineCharacter(operands, false);
      }
    });

    return directiveProcessor;
  }

  public Characters (String name) {
    List<String> names = new ArrayList<String>();

    if (name != null) {
      names.add(name);
    } else {
      Locale locale = Locale.getDefault();

      if (locale != null) {
        String language = locale.getLanguage();

        if (language != null) {
          String country = locale.getCountry();

          if (country != null) {
            names.add((language + "_" + country));
          }

          names.add(language);
        }
      }

      {
        String fallback = "en_US";
        if (!names.contains(fallback)) names.add(fallback);
      }
    }

    {
      final int size = names.size();

      for (int index=0; index<size; index+=1) {
        names.set(index, (names.get(index) + ".chars"));
      }
    }

    Log.d(LOG_TAG, "begin character definitions");
    makeInputProcessor().processInput(names);
    Log.d(LOG_TAG, "end character definitions");
  }

  public Characters () {
    this(null);
  }

  private final static Object CURRENT_CHARACTERS_LOCK = new Object();
  private static Characters currentCharacters = null;

  public static Characters getCharacters () {
    synchronized (CURRENT_CHARACTERS_LOCK) {
      if (currentCharacters == null) currentCharacters = new Characters();
    }

    return currentCharacters;
  }

  public static Characters setCharacters (Characters newCharacters) {
    synchronized (CURRENT_CHARACTERS_LOCK) {
      Characters oldCharacters = currentCharacters;
      currentCharacters = newCharacters;
      return oldCharacters;
    }
  }
}
