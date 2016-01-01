package org.liblouis;

import android.util.Log;

public class Tests {
  private final static String LOG_TAG = Tests.class.getName();

  private static void logOffsets (Translation trn) {
    int length;

    length = trn.getInputLength();
    for (int offset=0; offset<=length; offset+=1) {
      Log.d(LOG_TAG, String.format("in to out: %d -> %d", offset, trn.getOutputOffset(offset)));
    }

    length = trn.getOutputLength();
    for (int offset=0; offset<=length; offset+=1) {
      Log.d(LOG_TAG, String.format("out to in: %d -> %d", offset, trn.getInputOffset(offset)));
    }
  }

  public static void test (String table, CharSequence input) {
    BrailleTranslation trnb = new BrailleTranslation(table, input, 20, -1);
    CharSequence strb = trnb.getBrailleWithSpans();
    TextTranslation trnt = new TextTranslation(table, strb, 80, -1);
    CharSequence strt = trnt.getTextWithSpans();
    Log.d(LOG_TAG, "braille translation: " + strb);
    logOffsets(trnb);
    Log.d(LOG_TAG, "text translation: " + strt);
    logOffsets(trnt);
  }
}
