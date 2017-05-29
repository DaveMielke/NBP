package org.nbp.editor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;

import android.text.Editable;

public class ASCIIBrailleOperations extends ByteOperations {
  @Override
  protected int processBytes (Editable content, byte[] buffer, int count) {
    for (int index=0; index<count; index+=1) {
      byte brf = buffer[index];

      {
        char character = (char)(brf & 0XFF);

        if (Character.isISOControl(character) || Character.isWhitespace(character)) {
          content.append(character);
          continue;
        }
      }

      {
        char character = ASCIIBraille.toCharacter(brf);

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
        byte brf = ASCIIBraille.toAscii(character);

        if (brf == 0) {
          if ((character <= 0XFF) && Character.isISOControl(character)) {
            brf = (byte)character;
          } else if (Character.isWhitespace(character)) {
            brf = ' ';
          } else {
            continue;
          }
        }

        buffer.write(brf);
      }
    } finally {
      buffer.close();
    }
  }

  public ASCIIBrailleOperations () {
    super();
  }
}
