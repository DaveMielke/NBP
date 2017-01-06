package org.liblouis;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import java.io.File;

import android.util.Log;

public abstract class Tests {
  private final static String LOG_TAG = Tests.class.getName();

  private final static char OUT_OF_BOUNDS_CHARACTER = (char)0x28FF;

  private static void logOffset (
    CharSequence label,
    int inputOffset, char inputCharacter,
    int outputOffset, char outputCharacter
  ) {
    Log.d(LOG_TAG, String.format(
      "%s: %d->%d %c->%c %04X->%04X",
      label,
      inputOffset, outputOffset,
      inputCharacter, outputCharacter,
      (int)inputCharacter, (int)outputCharacter
    ));
  }

  public final static void logOutputOffsets (Translation translation) {
    CharSequence input = translation.getConsumedInput();
    int inputLength = input.length();

    char[] output = translation.getOutputAsArray();
    int outputLength = output.length;

    for (int inputOffset=0; inputOffset<inputLength; inputOffset+=1) {
      int outputOffset = translation.getOutputOffset(inputOffset);
      char outputCharacter =
        ((outputOffset >= 0) && (outputOffset < outputLength))?
        output[outputOffset]: OUT_OF_BOUNDS_CHARACTER;

      logOffset(
        "in->out",
        inputOffset, input.charAt(inputOffset),
        outputOffset, outputCharacter
      );
    }
  }

  public final static void logInputOffsets (Translation translation) {
    char[] output = translation.getOutputAsArray();
    int outputLength = output.length;

    CharSequence input = translation.getConsumedInput();
    int inputLength = input.length();

    for (int outputOffset=0; outputOffset<outputLength; outputOffset+=1) {
      int inputOffset = translation.getInputOffset(outputOffset);
      char inputCharacter =
        ((inputOffset >= 0) && (inputOffset < inputLength))?
        input.charAt(inputOffset): OUT_OF_BOUNDS_CHARACTER;

      logOffset(
        "out->in",
        outputOffset, output[outputOffset],
        inputOffset, inputCharacter
      );
    }
  }

  public final static void logOffsets (Translation translation) {
    logOutputOffsets(translation);
    logInputOffsets(translation);
  }

  public final static void translateText (TranslationTable table, CharSequence text) {
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

  public final static void translateBraille (TranslationTable table, CharSequence braille) {
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

  private final static void auditFile (Set<File> notFound, File file) {
    if (file.exists()) {
      notFound.remove(file);
    } else {
      Log.d(LOG_TAG, "file not found: " + file.getAbsolutePath());
    }
  }

  public static void auditTranslationTableEnumeration () {
    Log.d(LOG_TAG, "begin translation table enumeration audit");

    Set<File> notFound = new HashSet<File>();
    Collections.addAll(notFound, TranslationTable.getAllTableFiles());

    for (TranslationEnumeration value : TranslationEnumeration.values()) {
      TranslationTable table = value.getTranslationTable();

      {
        String identifier = value.name();

        if (!identifier.equals(identifier.toUpperCase())) {
          Log.d(LOG_TAG, "table identifier not all uppercase: " + identifier);
        }
      }

      {
        File forward = table.getForwardFileObject();
        auditFile(notFound, forward);

        File backward = table.getBackwardFileObject();
        if (backward != forward) auditFile(notFound, backward);
      }
    }

    for (File file : notFound) {
      Log.d(LOG_TAG, "table file not enumerated: " + file.getAbsolutePath());
    }

    Log.d(LOG_TAG, "end translation table enumeration audit");
  }

  protected Tests () {
  }
}
