package org.nbp.b2g.ui;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import java.util.Locale;

import org.nbp.common.CharacterUtilities;
import org.nbp.common.UnicodeUtilities;
import org.nbp.common.Braille;

import org.nbp.common.InputProcessor;
import org.nbp.common.DirectiveProcessor;

import android.util.Log;

public class Characters {
  private final static String LOG_TAG = Characters.class.getName();

  public final static String UNICODE_PREFIX = "U+";

  public static String toUnicodeString (char character) {
    return String.format("%s%04X", UNICODE_PREFIX, (int)character);
  }

  public static void logEvent (int priority, String tag, char character, String message) {
    Log.println(priority, tag, String.format(
      "%s: %s%04X", message, UNICODE_PREFIX, (int)character
    ));
  }

  public static void logProblem (String tag, char character, String problem) {
    logEvent(Log.WARN, tag, character, problem);
  }

  public static void logAction (String tag, char character, String action) {
    if (ApplicationSettings.LOG_ACTIONS) {
      logEvent(Log.VERBOSE, tag, character, action);
    }
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
      Byte dots = Braille.toCell(character);

      if (dots != null) {
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
      Character character = CharacterUtilities.getCharacter(operand);
      if (character != null) return character;
    }

    final int length = operand.length();
    if (length == 1) return operand.charAt(0);

    if ((length == 6) && operand.startsWith(UNICODE_PREFIX)) {
      String digits = operand.substring(UNICODE_PREFIX.length());
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

  private static Integer parseDots (String operand) {
    if (operand.equals("0")) return KeyMask.SPACE;
    return KeyMask.parseDots(operand);
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

    String dotsOperand = operands[index++];
    Integer keyMask = parseDots(dotsOperand);
    if (keyMask == null) return true;
    Byte dots = KeyMask.toDots(keyMask);

    if (dots == null) {
      Log.w(LOG_TAG, "not space or just dots: " + dotsOperand);
      return true;
    }

    if (index < operands.length) {
      Log.w(LOG_TAG, "too many operands");
    }

    if (forInput) {
      Character old = getCharacter(keyMask);

      if (old != null) {
        if (!old.equals(character)) {
          Log.w(LOG_TAG, "key combination already bound: " + dotsOperand);
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

    directiveProcessor.addDirective("alias", new DirectiveProcessor.DirectiveHandler() {
      @Override
      public boolean handleDirective (String[] operands) {
        int index = 0;

        if (index == operands.length) {
          Log.w(LOG_TAG, "new character not specified");
          return true;
        }

        String newCharacterOperand = operands[index++];
        Character newCharacter = parseCharacter(newCharacterOperand);
        if (newCharacter == null) return true;
        Byte newDots = getDots(newCharacter);

        if (index == operands.length) {
          Log.w(LOG_TAG, "existing character not specified");
          return true;
        }

        String existingCharacterOperand = operands[index++];
        Character existingCharacter = parseCharacter(existingCharacterOperand);
        if (existingCharacter == null) return true;
        Byte existingDots = getDots(existingCharacter);

        if (index < operands.length) {
          Log.w(LOG_TAG, "too many operands");
        }

        if (existingDots == null) {
          Log.w(LOG_TAG, "existing character not defined");
          return true;
        }

        if (newDots == null) {
          setDots(newCharacter, existingDots);
        } else if (!newDots.equals(existingDots)) {
          Log.w(LOG_TAG, "new character already defined");
        }

        return true;
      }
    });

    return directiveProcessor;
  }

  public Characters (String[] names) {
    {
      final int length = names.length;

      for (int index=0; index<length; index+=1) {
        names[index] = names[index] + ".chars";
      }
    }

    Log.d(LOG_TAG, "begin character definitions");
    makeInputProcessor().processInput(names);
    Log.d(LOG_TAG, "end character definitions");
  }

  public Characters (String name) {
    this(new String[] {name});
  }

  public Characters (Collection<String> names) {
    this(names.toArray(new String[names.size()]));
  }

  private static Collection<String> getDefaultNames () {
    Collection<String> names = new ArrayList<String>();
    Locale locale = Locale.getDefault();

    if (locale != null) {
      Log.i(LOG_TAG, ("locale: " + locale.toString()));
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

    return names;
  }

  public Characters () {
    this(getDefaultNames());
  }

  public static Characters getCharacters () {
    return ApplicationSettings.COMPUTER_BRAILLE.getCharacters();
  }
}
