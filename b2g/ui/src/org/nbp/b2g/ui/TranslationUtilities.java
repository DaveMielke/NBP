package org.nbp.b2g.ui;

import android.util.Log;

import org.liblouis.TranslationBuilder;
import org.liblouis.BrailleTranslation;
import org.liblouis.TextTranslation;

public abstract class TranslationUtilities {
  private final static String LOG_TAG = TranslationUtilities.class.getName();

  private final static int LENGTH_MULTIPLIER = 3;

  private static TranslationBuilder newTranslationBuilder (CharSequence input) {
    if (!ApplicationSettings.LITERARY_BRAILLE) return null;

    return new TranslationBuilder()
              .setInputCharacters(input)
              .setOutputLength(input.length() * LENGTH_MULTIPLIER)
              .setAllowLongerOutput(true)
              .setTranslationTable(ApplicationSettings.BRAILLE_CODE.getTranslationTable());
  }

  public static BrailleTranslation newBrailleTranslation (
    CharSequence text, boolean includeHighlighting
  ) {
    TranslationBuilder builder = newTranslationBuilder(text);
    if (builder == null) return null;
    builder.setIncludeHighlighting(includeHighlighting);
    return builder.newBrailleTranslation();
  }

  public static BrailleTranslation newBrailleTranslation (
    char text, boolean includeHighlighting
  ) {
    return newBrailleTranslation(Character.toString(text), includeHighlighting);
  }

  public static TextTranslation newTextTranslation (CharSequence braille) {
    TranslationBuilder builder = newTranslationBuilder(braille);
    if (builder == null) return null;
    return builder.newTextTranslation();
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
