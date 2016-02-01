package org.nbp.b2g.ui;

import android.util.Log;

import org.liblouis.TranslationBuilder;
import org.liblouis.BrailleTranslation;
import org.liblouis.TextTranslation;

public abstract class TranslationUtilities {
  private final static String LOG_TAG = TranslationUtilities.class.getName();

  private static int lengthMultiplier = 1;

  private static TranslationBuilder newTranslationBuilder () {
    if (!ApplicationSettings.LITERARY_BRAILLE) return null;

    return new TranslationBuilder()
              .setTranslationTable(ApplicationSettings.BRAILLE_CODE.getTranslationTable());
  }

  public static BrailleTranslation newBrailleTranslation (CharSequence text) {
    TranslationBuilder builder = newTranslationBuilder();
    if (builder == null) return null;

    builder.setInputCharacters(text);
    final int textLength = text.length();

    while (true) {
      builder.setOutputLength(textLength * lengthMultiplier);
      BrailleTranslation brl = builder.newBrailleTranslation();
      if (brl.getTextLength() == textLength) return brl;
      lengthMultiplier += 1;
    }
  }

  public static BrailleTranslation newBrailleTranslation (char text) {
    return newBrailleTranslation(Character.toString(text));
  }

  public static TextTranslation newTextTranslation (CharSequence braille) {
    TranslationBuilder builder = newTranslationBuilder();
    if (builder == null) return null;

    builder.setInputCharacters(braille);
    final int brailleLength = braille.length();

    while (true) {
      builder.setOutputLength(brailleLength * lengthMultiplier);
      TextTranslation txt = builder.newTextTranslation();
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
