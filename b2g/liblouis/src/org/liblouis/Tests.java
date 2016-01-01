package org.liblouis;

import java.io.File;

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

  public static void testTextTranslation (TranslationTable table, CharSequence input) {
    BrailleTranslation trnb = new BrailleTranslation(table, input, 20, -1);
    CharSequence strb = trnb.getBrailleWithSpans();
    TextTranslation trnt = new TextTranslation(table, strb, 80, -1);
    CharSequence strt = trnt.getTextWithSpans();
    Log.d(LOG_TAG, "braille translation: " + strb);
    logOffsets(trnb);
    Log.d(LOG_TAG, "text translation: " + strt);
    logOffsets(trnt);
  }

  public static void testTableDefinitions () {
    Log.d(LOG_TAG, "begin translation table definition test");

    for (TranslationTable table : TranslationTable.values()) {
      String symbol = table.name();
      String name = table.getName();
      File file = table.getFile();

      if (!symbol.equals(symbol.toUpperCase())) {
        Log.d(LOG_TAG, "table name not all uppercase: " + symbol);
      }

      if (!symbol.equals(name.toUpperCase().replace('-', '_'))) {
        Log.d(LOG_TAG, "table name doesn't match file name: " + name);
      }

      if (!file.exists()) {
        Log.d(LOG_TAG, "table not found: " + file.getAbsolutePath());
      }
    }

    Log.d(LOG_TAG, "end translation table definition test");
  }
}
