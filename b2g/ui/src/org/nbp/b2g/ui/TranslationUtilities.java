package org.nbp.b2g.ui;

import android.util.Log;

import org.liblouis.Louis;
import org.liblouis.BrailleTranslation;

public abstract class TranslationUtilities {
  private final static String LOG_TAG = TranslationUtilities.class.getName();

  public static BrailleTranslation newBrailleTranslation (CharSequence text) {
    if (!ApplicationSettings.LITERARY_BRAILLE) return null;

    return Louis.getBrailleTranslation(
      ApplicationSettings.BRAILLE_CODE.getTranslationTable(),
      text, (text.length() * 5), -1
    );
  }

  private TranslationUtilities () {
  }
}
