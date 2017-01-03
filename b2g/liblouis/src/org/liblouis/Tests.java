package org.liblouis;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import java.io.File;

import android.util.Log;

public class Tests {
  private final static String LOG_TAG = Tests.class.getName();

  private final static char OUT_OF_BOUNDS_CHARACTER = (char)0x28FF;

  public static void logOutputOffsets (Translation translation) {
    CharSequence input = translation.getConsumedInput();
    int inputLength = input.length();

    char[] output = translation.getOutputAsArray();
    int outputLength = output.length;

    for (int inputOffset=0; inputOffset<inputLength; inputOffset+=1) {
      int outputOffset = translation.getOutputOffset(inputOffset);
      char outputCharacter =
        ((outputOffset >= 0) && (outputOffset < outputLength))?
        output[outputOffset]: OUT_OF_BOUNDS_CHARACTER;

      Log.d(LOG_TAG, String.format(
        "in->out: %d->%d %c->%c",
        inputOffset, outputOffset,
        input.charAt(inputOffset), outputCharacter
      ));
    }
  }

  public static void logInputOffsets (Translation translation) {
    char[] output = translation.getOutputAsArray();
    int outputLength = output.length;

    CharSequence input = translation.getConsumedInput();
    int inputLength = input.length();

    for (int outputOffset=0; outputOffset<outputLength; outputOffset+=1) {
      int inputOffset = translation.getInputOffset(outputOffset);
      char inputCharacter =
        ((inputOffset >= 0) && (inputOffset < inputLength))?
        input.charAt(inputOffset): OUT_OF_BOUNDS_CHARACTER;

      Log.d(LOG_TAG, String.format(
        "out->in: %d->%d %c->%c",
        outputOffset, inputOffset,
        output[outputOffset], inputCharacter
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
