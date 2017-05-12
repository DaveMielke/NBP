package org.nbp.editor;

import android.text.Editable;

public class ASCIIBrailleOperations extends ByteOperations {
  @Override
  protected int processBytes (Editable content, byte[] buffer, int count) {
    for (int index=0; index<count; index+=1) {
      byte brf = buffer[index];

      switch (brf) {
        case ' ':
        case '\n':
          content.append((char)brf);
          break;

        default: {
          char character = ASCIIBraille.toCharacter(brf);
          if (character != 0) content.append(character);
          break;
        }
      }
    }

    return super.processBytes(content, buffer, count);
  }

  public ASCIIBrailleOperations () {
    super();
  }
}
