package org.liblouis;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import java.io.File;

import android.util.Log;

public class Tests {
  private final static String LOG_TAG = Tests.class.getName();

  private static void logOutputOffsets (Translation translation) {
    int length = translation.getInputLength();

    for (int offset=0; offset<=length; offset+=1) {
      Log.d(LOG_TAG, String.format("in to out: %d -> %d", offset, translation.getOutputOffset(offset)));
    }
  }

  private static void logInputOffsets (Translation translation) {
    int length = translation.getOutputLength();

    for (int offset=0; offset<=length; offset+=1) {
      Log.d(LOG_TAG, String.format("out to in: %d -> %d", offset, translation.getInputOffset(offset)));
    }
  }

  private static void logOffsets (Translation translation) {
    logOutputOffsets(translation);
    logInputOffsets(translation);
  }

  public static void translateText (TranslationTable table, CharSequence text) {
    Log.d(LOG_TAG, ("begin text translation: " + text));

    BrailleTranslation brl = new BrailleTranslation(table, text, 20, -1);
    CharSequence braille = brl.getBrailleWithSpans();
    Log.d(LOG_TAG, ("braille translation: " + braille));
    logOffsets(brl);

    TextTranslation txt = new TextTranslation(table, braille, 80, -1);
    CharSequence back = txt.getTextWithSpans();
    Log.d(LOG_TAG, ("text back-translation: " + back));
    logOffsets(txt);

    Log.d(LOG_TAG, "end text translation");
  }

  public static void auditTranslationTableEnumeration () {
    Log.d(LOG_TAG, "begin translation table enumeration audit");

    Set<File> unenumeratedTables = new HashSet<File>();
    Collections.addAll(unenumeratedTables, TranslationTable.getFiles());

    for (TranslationTable table : TranslationTable.values()) {
      String symbol = table.name();
      String name = table.getName();
      File file = table.getFile();

      if (!symbol.equals(symbol.toUpperCase())) {
        Log.d(LOG_TAG, "table enumeration not all uppercase: " + symbol);
      }

      if (!symbol.equals(name.toUpperCase().replace('-', '_'))) {
        Log.d(LOG_TAG, "table enumeration doesn't match file name: " + name);
      }

      if (!file.exists()) {
        Log.d(LOG_TAG, "table file not found: " + file.getAbsolutePath());
      }

      unenumeratedTables.remove(file);
    }

    for (File file : unenumeratedTables) {
      Log.d(LOG_TAG, "table file not enumerated: " + file.getAbsolutePath());
    }

    Log.d(LOG_TAG, "end translation table enumeration audit");
  }
}
