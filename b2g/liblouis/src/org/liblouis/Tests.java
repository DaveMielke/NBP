package org.liblouis;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import java.io.File;

import android.util.Log;

public class Tests {
  private final static String LOG_TAG = Tests.class.getName();

  public static void logOutputOffsets (Translation translation) {
    int length = translation.getInputLength();
    CharSequence input = translation.getConsumedInput();
    char[] output = translation.getOutputAsArray();

    for (int from=0; from<length; from+=1) {
      int to = translation.getOutputOffset(from);

      Log.d(LOG_TAG, String.format(
        "in->out: %d->%d %c->%c",
        from, to,
        input.charAt(from), output[to]
      ));
    }
  }

  public static void logInputOffsets (Translation translation) {
    int length = translation.getOutputLength();
    CharSequence input = translation.getConsumedInput();
    char[] output = translation.getOutputAsArray();

    for (int from=0; from<length; from+=1) {
      int to = translation.getInputOffset(from);

      Log.d(LOG_TAG, String.format(
        "out->in: %d->%d %c->%c",
        from, to,
        output[from], input.charAt(to)
      ));
    }
  }

  public static void logOffsets (Translation translation) {
    logOutputOffsets(translation);
    logInputOffsets(translation);
  }

  public static void translateText (TranslationTable table, CharSequence text) {
    Log.d(LOG_TAG, ("begin text translation test: " + text));

    BrailleTranslation brl = Louis.getBrailleTranslation(table, text);
    CharSequence braille = brl.getBrailleWithSpans();
    Log.d(LOG_TAG, ("braille translation: " + braille));
    logOffsets(brl);

    TextTranslation txt = Louis.getTextTranslation(table, braille);
    CharSequence back = txt.getTextWithSpans();
    Log.d(LOG_TAG, ("text back-translation: " + back));
    logOffsets(txt);

    Log.d(LOG_TAG, "end text translation test");
  }

  public static void translateBraille (TranslationTable table, CharSequence braille) {
    Log.d(LOG_TAG, ("begin braille translation test: " + braille));

     TextTranslation txt = Louis.getTextTranslation(table, braille);
    CharSequence text = txt.getTextWithSpans();
    Log.d(LOG_TAG, ("text translation: " + text));
    logOffsets(txt);

    BrailleTranslation brl = Louis.getBrailleTranslation(table, text);
    CharSequence back = brl.getBrailleWithSpans();
    Log.d(LOG_TAG, ("braille back-translation: " + back));
    logOffsets(brl);

    Log.d(LOG_TAG, "end braille translation test");
  }

  public static void auditTranslationTableEnumeration () {
    Log.d(LOG_TAG, "begin translation table enumeration audit");

    Set<File> unenumeratedTables = new HashSet<File>();
    Collections.addAll(unenumeratedTables, TranslationTable.getFiles());

    for (TranslationTable table : TranslationTable.values()) {
      String identifier = table.name();
      String name = table.getName();
      File file = table.getFile();

      if (!identifier.equals(identifier.toUpperCase())) {
        Log.d(LOG_TAG, "table identifier not all uppercase: " + identifier);
      }

      if (!identifier.equals(name.toUpperCase().replace('-', '_'))) {
        Log.d(LOG_TAG, "table identifier doesn't match file name: " + name);
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
