package org.liblouis;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.io.File;

import android.util.Log;

public abstract class Tests {
  private final static String LOG_TAG = Tests.class.getName();

  private Tests () {
  }

  private final static void log (String text) {
    Log.d(LOG_TAG, text);
  }

  private final static void log (CharSequence text) {
    log(text.toString());
  }

  public final static void logCharacters (
    CharSequence label, CharSequence characters,
    boolean asText, boolean asHexadecimal
  ) {
    if (asText) log(String.format("%s: %s", label, characters));

    if (asHexadecimal) {
      StringBuilder sb = new StringBuilder();
      sb.append(label);
      sb.append(':');
      int count = characters.length();

      for (int index=0; index<count; index+=1) {
        sb.append(String.format(" %04X", (int)characters.charAt(index)));
      }

      log(sb);
    }
  }

  private final static char OUT_OF_BOUNDS_CHARACTER = (char)0x28FF;

  private static void logOffset (
    CharSequence label,
    int inputOffset, char inputCharacter,
    int outputOffset, char outputCharacter
  ) {
    log(String.format(
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

  public final static void testTextTranslation (
    Translator translator, CharSequence text,
    boolean showText, boolean showHexadecimal, boolean showOffsets
  ) {
    log("begin text translation test");
    logCharacters("txt", text, showText, showHexadecimal);

    CharSequence braille;
    {
      BrailleTranslation translation = Louis.getBrailleTranslation(translator, text);
      braille = translation.getBrailleWithSpans();
      logCharacters("brl", braille, showText, showHexadecimal);
      if (showOffsets) logOffsets(translation);
    }

    {
      TextTranslation translation = Louis.getTextTranslation(translator, braille);
      logCharacters("bck", translation.getTextWithSpans(), showText, showHexadecimal);
      if (showOffsets) logOffsets(translation);
    }

    log("end text translation test");
  }

  public final static void testBrailleTranslation (
    Translator translator, CharSequence braille,
    boolean showText, boolean showHexadecimal, boolean showOffsets
  ) {
    log("begin braille translation test");
    logCharacters("brl", braille, showText, showHexadecimal);

     CharSequence text;
     {
       TextTranslation translation = Louis.getTextTranslation(translator, braille);
       text = translation.getTextWithSpans();
       logCharacters("txt", text, showText, showHexadecimal);
       if (showOffsets) logOffsets(translation);
     }

    {
      BrailleTranslation translation = Louis.getBrailleTranslation(translator, text);
      logCharacters("bck", translation.getBrailleWithSpans(), showText, showHexadecimal);
      if (showOffsets) logOffsets(translation);
    }

    log("end braille translation test");
  }

  public final static void testTextTranslation (
    TranslatorIdentifier identifier, CharSequence text,
    boolean showText, boolean showHexadecimal, boolean showOffsets
  ) {
    testTextTranslation(
      identifier.getTranslator(), text,
      showText, showHexadecimal, showOffsets
    );
  }

  public final static void testBrailleTranslation (
    TranslatorIdentifier identifier, CharSequence braille,
    boolean showText, boolean showHexadecimal, boolean showOffsets
  ) {
    testBrailleTranslation(
      identifier.getTranslator(), braille,
      showText, showHexadecimal, showOffsets
    );
  }

  private final static void auditFile (Set<File> notFound, InternalTable table) {
    File file = table.getFileObject();

    if (file.exists()) {
      notFound.remove(file);
    } else {
      Log.d(LOG_TAG, "file not found: " + file.getAbsolutePath());
    }
  }

  public static void auditTranslatorIdentifiers () {
    Log.d(LOG_TAG, "begin translator identifier audit");

    Set<File> notFound = new HashSet<File>();
    Collections.addAll(notFound, InternalTable.getAllFiles());

    for (TranslatorIdentifier identifier : TranslatorIdentifier.values()) {
      {
        String name = identifier.name();

        if (!name.equals(name.toUpperCase())) {
          Log.d(LOG_TAG, ("translator identifier not all uppercase: " + name));
        }
      }

      {
        Translator translator = identifier.getTranslator();

        if (translator instanceof InternalTranslator) {
          InternalTranslator internal = (InternalTranslator)translator;

          InternalTable forward = internal.getForwardTable();
          auditFile(notFound, forward);

          InternalTable backward = internal.getBackwardTable();
          if (backward != forward) auditFile(notFound, backward);
        }
      }
    }

    for (File file : notFound) {
      Log.d(LOG_TAG, "unknown table file: " + file.getAbsolutePath());
    }

    Log.d(LOG_TAG, "end translator identifier audit");
  }
}
