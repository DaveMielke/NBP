package org.nbp.b2g.ui;

import android.util.Log;

import org.liblouis.Louis;
import org.liblouis.TranslationTable;
import org.liblouis.BrailleTranslation;
import org.liblouis.TextTranslation;

public abstract class TranslationUtilities {
  private final static String LOG_TAG = TranslationUtilities.class.getName();

  private static int lengthMultiplier = 1;

  private static TranslationTable getTranslationTable () {
    if (!ApplicationSettings.LITERARY_BRAILLE) return null;
    return ApplicationSettings.BRAILLE_CODE.getTranslationTable();
  }

  public static BrailleTranslation newBrailleTranslation (CharSequence text) {
    TranslationTable table = getTranslationTable();
    if (table == null) return null;
    final int textLength = text.length();

    while (true) {
      BrailleTranslation brl = Louis.getBrailleTranslation(
        table, text, (textLength * lengthMultiplier), -1
      );

      if (brl.getTextLength() == textLength) return brl;
      lengthMultiplier += 1;
    }
  }

  public static BrailleTranslation newBrailleTranslation (char text) {
    return newBrailleTranslation(Character.toString(text));
  }

  public static TextTranslation newTextTranslation (CharSequence braille) {
    TranslationTable table = getTranslationTable();
    if (table == null) return null;
    final int brailleLength = braille.length();

    while (true) {
      TextTranslation txt = Louis.getTextTranslation(
        table, braille, (brailleLength * lengthMultiplier), -1
      );

      if (txt.getBrailleLength() == brailleLength) return txt;
      lengthMultiplier += 1;
    }
  }

  public static TextTranslation newTextTranslation (char braille) {
    return newTextTranslation(Character.toString(braille));
  }

  public static void cacheBraille (char character) {
    String braille = Character.toString(character);
    TextTranslation translation = newTextTranslation(braille);
    CharSequence text = translation.getTextWithSpans();
    TranslationCache.put(text, translation);
  }

  private TranslationUtilities () {
  }
}
