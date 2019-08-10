package org.nbp.editor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;

import android.text.Editable;

import org.liblouis.Translator;
import org.liblouis.TranslatorIdentifier;
import org.liblouis.TranslationBuilder;
import org.liblouis.BrailleTranslation;
import org.liblouis.TextTranslation;

public class ASCIIBrailleOperations extends ByteOperations {
  private final static BrailleMode.Conversions getConversions () {
    return ApplicationSettings.BRAILLE_MODE.getConversions();
  }

  private final static Translator getTranslator () throws IOException {
    BrailleCode code = ApplicationSettings.BRAILLE_CODE;;

    try {
      return code.getTranslator();
    } catch (SecurityException exception) {
      throw new IOException(
        String.format(
          "braille code not available: %s: %s",
          code.getDescription(), exception.getMessage()
        )
      );
    }
  }

  private final static TranslationBuilder getTranslationBuilder () throws IOException {
    Translator translator = getTranslator();
    if (translator == null) return null;

    TranslationBuilder builder = new TranslationBuilder();
    builder.setTranslator(translator);
    builder.setAllowLongerOutput(true);
    builder.setIncludeHighlighting(true);
    return builder;
  }

  @Override
  protected int processBytes (Editable content, byte[] buffer, int count) {
    BrailleMode.Conversions conversions = getConversions();

    for (int index=0; index<count; index+=1) {
      byte symbol = buffer[index];

      {
        char character = (char)(symbol & 0XFF);

        if (Character.isISOControl(character) || Character.isWhitespace(character)) {
          switch (character) {
            default:
              content.append(character);
              /* fall through */
            case '\r':
              continue;
          }
        }
      }

      {
        char character = conversions.symbolToChar(symbol);

        if (character != 0) {
          content.append(character);
          continue;
        }
      }
    }

    return count;
  }

  private final int translateInputCharacters (
    TranslationBuilder builder, Editable content, int from, int to
  ) {
    if (builder == null) return 0;
    if (to == from) return 0;

    CharSequence characters = content.subSequence(from, to);
    builder.setInputCharacters(characters);
    builder.setOutputLength(characters.length() * 2);

    TextTranslation translation = new TextTranslation(builder);
    CharSequence replacement = translation.getTextWithSpans();
    content.replace(from, to, replacement);
    return replacement.length() - (to - from);
  }

  private final boolean isSpecialCharacter (char character) {
    switch (character) {
      case '\f':
      case '\n':
      case '\r':
      case '\t':
        return true;

      default:
      case ' ':
        return false;
    }
  }

  @Override
  protected void endBytes (Editable content) throws IOException {
    TranslationBuilder builder = getTranslationBuilder();
    int length = content.length();
    int from = 0;
    int to;

    for (to=0; to<length; to+=1) {
      char character = content.charAt(to);

      if (isSpecialCharacter(character)) {
        int adjustment = translateInputCharacters(builder, content, from, to);
        length += adjustment;
        to += adjustment;
        from = to + 1;
      }
    }

    translateInputCharacters(builder, content, from, to);
  }

  private final void write (
    OutputStream stream, TranslationBuilder builder,
    CharSequence content, int from, int to
  ) throws IOException {
    if (to > from) {
      content = content.subSequence(from, to);

      if (builder != null) {
        builder.setInputCharacters(content);
        builder.setOutputLength(content.length() * 2);

        BrailleTranslation translation = new BrailleTranslation(builder);
        content = translation.getBrailleAsString();
      }

      int length = content.length();

      for (int index=0; index<length; index+=1) {
        char character = content.charAt(index);
        byte symbol = ASCIIBraille.charToSymbol(character);

        if (symbol == 0) {
          if ((character <= 0XFF) && Character.isISOControl(character)) {
            symbol = (byte)character;
          } else if (Character.isWhitespace(character)) {
            symbol = ' ';
          } else {
            continue;
          }
        }

        stream.write(symbol);
      }
    }
  }

  @Override
  public void write (OutputStream stream, CharSequence content) throws IOException {
    if (!(stream instanceof BufferedOutputStream)) {
      stream = new BufferedOutputStream(stream);
    }

    try {
      TranslationBuilder builder = getTranslationBuilder();
      int length = content.length();
      int from = 0;
      int to;

      for (to=0; to<length; to+=1) {
        char character = content.charAt(to);

        if (isSpecialCharacter(character)) {
          write(stream, builder, content, from, to);
          from = to + 1;
          stream.write(character);
        }
      }

      if (to > from) write(stream, builder, content, from, to);
    } finally {
      stream.close();
    }
  }

  public ASCIIBrailleOperations () {
    super();
  }
}
