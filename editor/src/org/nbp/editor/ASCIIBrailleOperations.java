package org.nbp.editor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;

import android.text.Editable;

public class ASCIIBrailleOperations extends ByteOperations {
  @Override
  protected int processBytes (Editable content, byte[] buffer, int count) {
    for (int index=0; index<count; index+=1) {
      byte ascii = buffer[index];

      {
        char character = (char)(ascii & 0XFF);

        if (Character.isISOControl(character) || Character.isWhitespace(character)) {
          content.append(character);
          continue;
        }
      }

      {
        char character = ApplicationSettings.BRAILLE_MODE.getConversions().asciiToChar(ascii);

        if (character != 0) {
          content.append(character);
          continue;
        }
      }
    }

    return count;
  }

  @Override
  public void write (OutputStream stream, CharSequence content) throws IOException {
    OutputStream buffer = new BufferedOutputStream(stream);

    try {
      int count = content.length();

      for (int index=0; index<count; index+=1) {
        char character = content.charAt(index);
        byte ascii = ASCIIBraille.charToAscii(character);

        if (ascii == 0) {
          if ((character <= 0XFF) && Character.isISOControl(character)) {
            ascii = (byte)character;
          } else if (Character.isWhitespace(character)) {
            ascii = ' ';
          } else {
            continue;
          }
        }

        buffer.write(ascii);
      }
    } finally {
      buffer.close();
    }
  }

  public ASCIIBrailleOperations () {
    super();
  }
}
